package com.TrainingSouls.Service;

import com.TrainingSouls.Configuration.PaypalProperties;
import com.TrainingSouls.DTO.Request.PurchaseRequest;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.StoreItem;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserItem;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.PointsTransactionRepository;
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
    private final PointsTransactionRepository pointsTransactionRepository;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final PaypalProperties paypalProperties;



    @Transactional
    public String purchaseItem(HttpServletRequest request, Integer itemId){
        //lay userId tu header
        Long userId = JWTUtils.getSubjectFromRequest(request);

        // lay user tu DB
        User user = userRepository.findById(userId).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        StoreItem item = storeItemService.getById(itemId);


        if (user.getPoints() < item.getPointsRequired()) {
            return "Không đủ points để mua!";
        }


        //tru point
        user.setPoints(user.getPoints() - item.getPointsRequired());

        // Đảm bảo danh sách purchasedItems không null
        if (user.getPurchasedItems() == null) {
            user.setPurchasedItems(new ArrayList<>());
        }

        UserItem userItem = new UserItem();
        userItem.setUser(user);
        userItem.setItemId(itemId);
        userItem.setExpirationDate(LocalDate.now().plusDays(item.getDurationInDays()));
        user.getPurchasedItems().add(userItem);

        userRepository.save(user);


        // luu lich su giao dich
        PointsTransaction pointsTransaction = new PointsTransaction();
        pointsTransaction.setUser(user);
        pointsTransaction.setPoints(item.getPointsRequired());
        pointsTransaction.setType(PointsTransaction.TransactionType.SPEND);
        pointsTransaction.setDescription("Mua item" + " " + item.getName() + "bằng Point");
        pointsTransaction.setDate(Instant.now());
        pointsTransaction.setStatus(PointsTransaction.TransactionStatus.SUCCESS);

        pointsTransactionRepository.save(pointsTransaction);

        return "Purchase Success";
    }


    @Transactional
    public void completedPurchaseItem(HttpServletRequest request, PurchaseRequest dto){
        Long userId = JWTUtils.getSubjectFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        String accessToken = getPaypalAccessToken();
        boolean isValid = verifyPaypalOrder(dto.getOrderId(), accessToken);
        if (!isValid) {
            throw new AppException(ErrorCode.PAYMENT_VERIFICATION_FAILED);
        }

        StoreItem item = storeItemService.getById(dto.getItemId());

        PointsTransaction transaction = new PointsTransaction();
        transaction.setUser(user);
        transaction.setPoints(item.getPointsRequired());
        transaction.setType(PointsTransaction.TransactionType.SPEND);
        transaction.setDescription("Mua item " + item.getName() + " qua PayPal");
        transaction.setDate(Instant.now());
        transaction.setStatus(PointsTransaction.TransactionStatus.SUCCESS);
        pointsTransactionRepository.save(transaction);

        UserItem userItem = new UserItem();
        userItem.setUser(user);
        userItem.setItemId(dto.getItemId());
        userItem.setExpirationDate(LocalDate.now().plusDays(item.getDurationInDays()));
        user.getPurchasedItems().add(userItem);
        userRepository.save(user);
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



    @Scheduled(cron = "0 5 0 * * *")
    @Transactional
    public void deleteExpiredItems() {
        LocalDate now = LocalDate.now();
        List<UserItem> expiredItems = userItemRepository.findByExpirationDateBefore(now);

        userItemRepository.deleteAll(expiredItems);
    }


}
