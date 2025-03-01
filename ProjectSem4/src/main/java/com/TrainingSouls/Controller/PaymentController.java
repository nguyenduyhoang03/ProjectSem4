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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }


    @GetMapping("/vn-pay-callback")
    public ResponseObject<PaymentDTO.VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        return paymentService.handleVNPayCallback(request);
    }

}
