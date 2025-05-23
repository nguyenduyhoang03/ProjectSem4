package com.TrainingSouls.Service;

import com.TrainingSouls.Entity.PurchaseTransaction;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;


    @Async
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi email xác nhận", e);
        }
    }

    @Async
    public void sendInvoiceEmail(PurchaseTransaction transaction) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Lấy thông tin từ transaction
            Long id = transaction.getId();
            String to = transaction.getUser().getEmail();
            String fullName = transaction.getUser().getName();
            String itemName = transaction.getItem().getName();
            BigDecimal amount = transaction.getAmount();
            String paymentMethod = transaction.getPaymentMethod().name();
            LocalDateTime transactionTime = transaction.getTransactionTime();
            String status = transaction.getStatus().name();

            // Tiêu đề và nội dung email
            helper.setTo(to);
            helper.setSubject("Hóa đơn mua hàng - Training Souls");

            StringBuilder sb = new StringBuilder();

            sb.append("<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9;\">");
            sb.append("<style>");
            sb.append("@media only screen and (max-width: 600px) {");
            sb.append("  table { width: 100% !important; }");
            sb.append("  thead { display: none; }");
            sb.append("  tr { display: block; margin-bottom: 10px; }");
            sb.append("  td { display: block; text-align: right; padding-left: 50%; position: relative; }");
            sb.append("  td::before { position: absolute; left: 10px; top: 10px; white-space: nowrap; font-weight: bold; text-align: left; }");
            sb.append("  td:nth-of-type(1)::before { content: 'Mã giao dịch'; }");
            sb.append("  td:nth-of-type(2)::before { content: 'Sản phẩm'; }");
            sb.append("  td:nth-of-type(3)::before { content: 'Thanh toán'; }");
            sb.append("  td:nth-of-type(4)::before { content: 'Số tiền'; }");
            sb.append("  td:nth-of-type(5)::before { content: 'Thời gian'; }");
            sb.append("}");
            sb.append("</style>");

            sb.append("<h2 style=\"color: #2c3e50; text-align: center;\">Hóa đơn mua hàng</h2>");
            sb.append("<p>Xin chào <strong>").append(fullName).append("</strong>,</p>");
            sb.append("<p>Cảm ơn bạn đã mua hàng tại <strong style='color:#16a085;'>Training Souls</strong>. Dưới đây là chi tiết giao dịch của bạn:</p>");

            sb.append("<table style=\"width: 100%; border-collapse: collapse; margin-top: 15px;\">");
            sb.append("<thead>");
            sb.append("<tr style=\"background-color: #16a085; color: white;\">");
            sb.append("<th style=\"padding: 10px; border: 1px solid #ccc;\">Mã giao dịch</th>");
            sb.append("<th style=\"padding: 10px; border: 1px solid #ccc;\">Sản phẩm</th>");
            sb.append("<th style=\"padding: 10px; border: 1px solid #ccc;\">Thanh toán</th>");
            sb.append("<th style=\"padding: 10px; border: 1px solid #ccc;\">Số tiền</th>");
            sb.append("<th style=\"padding: 10px; border: 1px solid #ccc;\">Trạng thái</th>");
            sb.append("<th style=\"padding: 10px; border: 1px solid #ccc;\">Thời gian</th>");
            sb.append("</tr>");
            sb.append("</thead>");

            sb.append("<tbody>");
            sb.append("<tr style=\"text-align: center; background-color: #ffffff;\">");
            sb.append("<td style=\"padding: 10px; border: 1px solid #ccc;\">").append(id).append("</td>");
            sb.append("<td style=\"padding: 10px; border: 1px solid #ccc;\">").append(itemName).append("</td>");
            sb.append("<td style=\"padding: 10px; border: 1px solid #ccc;\">").append(paymentMethod).append("</td>");
            sb.append("<td style=\"padding: 10px; border: 1px solid #ccc; color: #e67e22;\"><strong>").append(amount).append(" $</strong></td>");
            sb.append("<td style=\"padding: 10px; border: 1px solid #ccc;\">").append(transactionTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("</td>");
            sb.append("</tr>");
            sb.append("</tbody>");
            sb.append("</table>");

            sb.append("<p style=\"margin-top: 20px;\">Trân trọng,</p>");
            sb.append("<p><i>Training Souls Team</i></p>");
            sb.append("</div>");

            helper.setText(sb.toString(), true); // Gửi dưới dạng HTML
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Lỗi khi gửi email hóa đơn", e);
        }
    }



}


