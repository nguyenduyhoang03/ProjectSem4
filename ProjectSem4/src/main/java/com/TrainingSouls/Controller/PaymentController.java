package com.TrainingSouls.Controller;


import com.TrainingSouls.DTO.Response.PaymentDTO;
import com.TrainingSouls.DTO.Response.ResponseObject;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.PointsTransactionRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/payment")

public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPurchase(request));
    }


    @GetMapping("/vn-pay-callback")
    public ResponseEntity<String> handleVNPayCallback(HttpServletRequest request) {
        paymentService.handleVNPayCallback(request);

        String responseCode = request.getParameter("vnp_ResponseCode");
        String transactionId = request.getParameter("vnp_TxnRef");
        String transactionNo = request.getParameter("vnp_TransactionNo");
        String amount = request.getParameter("vnp_Amount");
        String payDate = request.getParameter("vnp_PayDate");

        // Xác định trạng thái giao dịch
        boolean isSuccess = "00".equals(responseCode);
        String status = isSuccess ? "Giao dịch thành công" : "Giao dịch thất bại";
        String icon = isSuccess ? "✅" : "❌";
        String titleColor = isSuccess ? "green" : "red";

        // Xử lý thời gian giao dịch
        String formattedDate = formatVNPayDate(payDate);

        // Xử lý số tiền (VNPay trả về nhân 100)
        double amountVND = Double.parseDouble(amount) / 100;

        String html = "<html lang=\"vi\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Thông báo - VNPay</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; text-align: center; background-color: #f8f8f8; }\n" +
                "        .container { max-width: 500px; margin: 50px auto; background: white; padding: 20px;\n" +
                "            border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }\n" +
                "        .icon { font-size: 50px; color: " + titleColor + "; }\n" +
                "        .title { font-size: 22px; color: " + titleColor + "; font-weight: bold; }\n" +
                "        .message { font-size: 16px; color: #555; margin: 10px 0; }\n" +
                "        .details { background: #f1f1f1; padding: 10px; border-radius: 5px; margin-top: 15px; }\n" +
                "        .footer { margin-top: 20px; font-size: 14px; color: #777; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"icon\">" + icon + "</div>\n" +
                "        <div class=\"title\">Thông báo</div>\n" +
                "        <div class=\"message\">" + status + "</div>\n" +
                "        <div class=\"details\">\n" +
                "            <p><strong>Mã giao dịch:</strong> " + transactionId + "</p>\n" +
                "            <p><strong>Mã VNPay:</strong> " + transactionNo + "</p>\n" +
                "            <p><strong>Số tiền:</strong> " + amountVND + " VNĐ</p>\n" +
                "            <p><strong>Thời gian:</strong> " + formattedDate + "</p>\n" +
                "            <a href=\"http://localhost:3000\">Quay lại trang chủ</a>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            📞 <a href=\"tel:1900555577\">1900.5555.77</a> | 📧 <a href=\"mailto:hotrovnpay@vnpay.vn\">hotrovnpay@vnpay.vn</a>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
    }

    /**
     * Chuyển đổi thời gian từ định dạng VNPay (yyyyMMddHHmmss) thành dd/MM/yyyy HH:mm:ss
     */
    private String formatVNPayDate(String vnpPayDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = inputFormat.parse(vnpPayDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            return "Không xác định";
        }
    }



}
