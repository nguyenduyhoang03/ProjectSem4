package com.TrainingSouls.Service;

import com.TrainingSouls.Configuration.VNPAYConfig;
import com.TrainingSouls.DTO.Response.*;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Utils.JWTUtils;
import com.TrainingSouls.Utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final VNPAYConfig vnPayConfig;

    private final UserService userService;

    private final PointsTransactionService pointsTransactionService;

    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Long.parseLong(request.getParameter("amount"));

        // Lấy userId từ token JWT
        Long userId = JWTUtils.getSubjectFromRequest(request);

        // Lấy thông tin user từ database
        User user = userService.getUserById(userId);

        // Lưu giao dịch vào database với trạng thái PENDING
//        PointsTransaction transaction = new PointsTransaction();
//        transaction.setUser(user);
//        transaction.setPoints((int) amount);
//        transaction.setType(PointsTransaction.TransactionType.EARN);
//        transaction.setDescription("Nạp " + amount + " points qua VNPay");
//        transaction.setDate(Instant.now());
//        transaction.setStatus(PointsTransaction.TransactionStatus.PENDING);

        PointsTransaction transaction = pointsTransactionService.create(user,amount);

        // Lấy TransactionID do database tạo ra
        Integer transactionId = transaction.getTransactionId();

        // Gọi VNPay
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount * 100));
        vnpParamsMap.put("vnp_TxnRef", String.valueOf(transactionId)); // Dùng TransactionID từ database
        vnpParamsMap.put("vnp_BankCode", "NCB");
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Build query URL
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

        System.out.println("VNPay Callback received! Transaction ID: " + transactionId + ", Response Code: " + responseCode);

        // Tìm giao dịch trong database
        PointsTransaction pointsTransaction = pointsTransactionService.getById(transactionId);

        if (pointsTransaction == null) {
            System.out.println("Transaction not found in database!");
            return new ResponseObject<>(HttpStatus.NOT_FOUND, "Transaction not found!", null);
        }

        System.out.println("Transaction found: " + pointsTransaction);

        if ("00".equals(responseCode)) { // Thanh toán thành công

            Long userId = pointsTransaction.getUser().getUserID();

            userService.addPoints(userId, pointsTransaction.getPoints());

            pointsTransaction.setStatus(PointsTransaction.TransactionStatus.SUCCESS);
        } else {
            pointsTransaction.setStatus(PointsTransaction.TransactionStatus.FAILED);
        }


        return new ResponseObject<>(HttpStatus.OK, "Payment processed",
                new PaymentDTO.VNPayResponse(responseCode, "Transaction updated", "http://localhost:3000"));
    }


}
