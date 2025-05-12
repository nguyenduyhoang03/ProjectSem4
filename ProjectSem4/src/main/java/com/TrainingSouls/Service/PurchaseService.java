package com.TrainingSouls.Service;

import com.TrainingSouls.Configuration.PaypalProperties;
import com.TrainingSouls.Configuration.StripeProperties;
import com.TrainingSouls.DTO.Request.PurchaseRequest;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserItem;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.PointsTransactionRepository;
import com.TrainingSouls.Repository.StoreItemRepository;
import com.TrainingSouls.Repository.UserItemRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class PurchaseService {
    private final StoreItemService storeItemService;
    private final StoreItemRepository storeItemRepository;
    private final PointsTransactionRepository pointsTransactionRepository;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final PaypalProperties paypalProperties;
    private final StripeProperties stripeProperties;



    @Transactional
    public String purchaseItemByPoints(HttpServletRequest request, Integer itemId) {
        Long userId = JWTUtils.getSubjectFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        StoreItem item = storeItemService.getById(itemId);

        if (user.getPoints() < item.getPointsRequired()) {
            return "Không đủ points để mua!";
        }

        // Trừ point
        user.setPoints(user.getPoints() - item.getPointsRequired());
        userRepository.save(user);

        // Ghi log giao dịch
        PointsTransaction transaction = new PointsTransaction();
        transaction.setUser(user);
        transaction.setPoints(item.getPointsRequired());
        transaction.setType(PointsTransaction.TransactionType.SPEND);
        transaction.setDescription("Mua item " + item.getName() + " bằng Point");
        transaction.setDate(Instant.now());
        transaction.setStatus(PointsTransaction.TransactionStatus.SUCCESS);
        transaction.setItemId(itemId);
        pointsTransactionRepository.save(transaction);

        // Gọi hàm xử lý chính
        handleSuccessfulPurchase(userId, itemId);

        return "Purchase Success";
    }


    public void handleSuccessfulPurchase(Long userId, Integer itemId) {
        User user = userRepository.findById(userId).orElseThrow();
        StoreItem storeItem = storeItemRepository.findById(itemId).orElseThrow();

        String itemName = storeItem.getName();
        Integer duration = storeItem.getDurationInDays();
        LocalDate now = LocalDate.now();

        if (duration != null && duration > 0) {
            // Gói có hạn dùng
            Optional<UserItem> existingItem = userItemRepository.findByUserUserIDAndItemId(userId, itemId);
            if (existingItem.isPresent()) {
                UserItem item = existingItem.get();
                LocalDate currentExp = item.getExpirationDate();
                item.setExpirationDate(currentExp != null && !currentExp.isBefore(now)
                        ? currentExp.plusDays(duration)
                        : now.plusDays(duration));
                userItemRepository.save(item);
            } else {
                UserItem newItem = new UserItem();
                newItem.setUser(user);
                newItem.setItemId(itemId);
                newItem.setExpirationDate(now.plusDays(duration));
                userItemRepository.save(newItem);
            }

            if (storeItem.getItemType().equals(StoreItem.StoreItemType.SUBSCRIPTION)) {
                user.setAccountType("Premium");
                userRepository.save(user);
            }

        } else {
            // Item thường không có hạn
            UserItem item = new UserItem();
            item.setUser(user);
            item.setItemId(itemId);
            userItemRepository.save(item);
        }

        // Trừ số lượng
        storeItem.setQuantity(storeItem.getQuantity() - 1);
        storeItemRepository.save(storeItem);
    }



    @Transactional
    public void completePaypalPurchase(HttpServletRequest request, PurchaseRequest dto) {
        Long userId = JWTUtils.getSubjectFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        String accessToken = getPaypalAccessToken();
        boolean isValid = verifyPaypalOrder(dto.getOrderId(), accessToken);
        if (!isValid) {
            throw new AppException(ErrorCode.PAYMENT_VERIFICATION_FAILED);
        }

        StoreItem item = storeItemService.getById(dto.getItemId());

        // Ghi log giao dịch
        PointsTransaction transaction = new PointsTransaction();
        transaction.setUser(user);
        transaction.setPoints(item.getPointsRequired());
        transaction.setType(PointsTransaction.TransactionType.SPEND);
        transaction.setDescription("Mua item " + item.getName() + " qua PayPal");
        transaction.setDate(Instant.now());
        transaction.setStatus(PointsTransaction.TransactionStatus.SUCCESS);
        transaction.setItemId(dto.getItemId());
        pointsTransactionRepository.save(transaction);

        // Gọi xử lý chính
        handleSuccessfulPurchase(userId, dto.getItemId());
    }



    private String getPaypalAccessToken() {
        String clientId = paypalProperties.getClientId();
        String secretId = paypalProperties.getSecretId();
        String auth = Base64.getEncoder().encodeToString((clientId + ":" + secretId).getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, secretId);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity("https://api-m.sandbox.paypal.com/v1/oauth2/token", request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    private boolean verifyPaypalOrder(String orderId, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api-m.sandbox.paypal.com/v2/checkout/orders/" + orderId,
                HttpMethod.GET,
                entity,
                Map.class
        );

        String status = (String) response.getBody().get("status");
        return "COMPLETED".equalsIgnoreCase(status);
    }

    private boolean verifyStripePayment(String orderId) {
        String secretKey = stripeProperties.getSecretKey();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(secretKey);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.stripe.com/v1/payment_intents/" + orderId,
                HttpMethod.GET,
                entity,
                Map.class
        );

        String status = (String) response.getBody().get("status");
        return "succeeded".equalsIgnoreCase(status);
    }

    @Transactional
    public void completeStripePurchase(HttpServletRequest request, PurchaseRequest dto) {
        Long userId = JWTUtils.getSubjectFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        boolean isValid = verifyStripePayment(dto.getOrderId());
        if (!isValid) {
            throw new AppException(ErrorCode.PAYMENT_VERIFICATION_FAILED);
        }

        StoreItem item = storeItemService.getById(dto.getItemId());

        // Ghi log giao dịch
        PointsTransaction transaction = new PointsTransaction();
        transaction.setUser(user);
        transaction.setPoints(item.getPointsRequired());
        transaction.setType(PointsTransaction.TransactionType.SPEND);
        transaction.setDescription("Mua item " + item.getName() + " qua Stripe");
        transaction.setDate(Instant.now());
        transaction.setStatus(PointsTransaction.TransactionStatus.SUCCESS);
        transaction.setItemId(dto.getItemId());
        pointsTransactionRepository.save(transaction);

        // Xử lý chính
        handleSuccessfulPurchase(userId, dto.getItemId());
    }




    @Scheduled(cron = "0 5 0 * * *")
    @Transactional
    public void deleteExpiredItems() {
        LocalDate now = LocalDate.now();
        List<UserItem> expiredItems = userItemRepository.findByExpirationDateBefore(now);

        userItemRepository.deleteAll(expiredItems);
    }


}
