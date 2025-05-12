package com.TrainingSouls.Service;

import com.TrainingSouls.Configuration.VNPAYConfig;
import com.TrainingSouls.DTO.Response.*;
import com.TrainingSouls.Entity.*;
import com.TrainingSouls.Repository.UserItemRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import com.TrainingSouls.Utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final VNPAYConfig vnPayConfig;
    private final UserService userService;
    private final PointsTransactionService pointsTransactionService;
    private final StoreItemService storeItemService;
    private final PurchaseService purchaseService;



    //thanh toan truc tiep thay vi dung point
    public PaymentDTO.VNPayResponse createVnPayPurchase(HttpServletRequest request) {
        Long userId = JWTUtils.getSubjectFromRequest(request);
        Integer itemId = Integer.valueOf(request.getParameter("itemId"));

        User user = userService.getUserById(userId);
        StoreItem item = storeItemService.getById(itemId);

        if (user == null || item == null) {
            throw new RuntimeException("User hoặc item không tồn tại");
        }

        PointsTransaction transaction = pointsTransactionService.createPurchaseTransaction(user, item);
        Integer transactionId = transaction.getTransactionId();

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf((item.getPrice() * 100))); // VNPay yêu cầu * 100
        vnpParamsMap.put("vnp_TxnRef", String.valueOf(transactionId));
        vnpParamsMap.put("vnp_BankCode", "NCB");
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return new PaymentDTO.VNPayResponse("ok", "success", paymentUrl);
    }



    @Transactional
    public ResponseObject<PaymentDTO.VNPayResponse> handleVNPayCallback(HttpServletRequest request) {
        Integer transactionId = Integer.valueOf(request.getParameter("vnp_TxnRef"));
        String responseCode = request.getParameter("vnp_ResponseCode");

        PointsTransaction transaction = pointsTransactionService.getById(transactionId);
        if (transaction == null) {
            return new ResponseObject<>(HttpStatus.NOT_FOUND, "Không tìm thấy giao dịch", null);
        }

        if ("00".equals(responseCode)) {
            transaction.setStatus(PointsTransaction.TransactionStatus.SUCCESS);


            Long userId = transaction.getUser().getUserID();
            Integer itemId = transaction.getItemId();

            purchaseService.handleSuccessfulPurchase(userId, itemId);
        } else {
            transaction.setStatus(PointsTransaction.TransactionStatus.FAILED);

        }

        return new ResponseObject<>(
                HttpStatus.OK,
                "Payment processed",
                new PaymentDTO.VNPayResponse(responseCode, "Transaction updated", "http://localhost:3000")
        );
    }



}
