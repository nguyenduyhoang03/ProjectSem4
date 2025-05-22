package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.AuthenticationRequest;

import com.TrainingSouls.DTO.Request.IntrospectRequest;
import com.TrainingSouls.DTO.Response.ApiResponse;
import com.TrainingSouls.DTO.Response.AuthenticationResponse;
import com.TrainingSouls.DTO.Response.IntrospectResponse;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.VerificationToken;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Repository.VerificationTokenRepository;
import com.TrainingSouls.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    UserRepository userRepository;
    VerificationTokenRepository verificationTokenRepository;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        var result = authenticationService.authenticate(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody IntrospectRequest token) throws ParseException, JOSEException {
        var result = authenticationService.refreshAccessToken(token.getToken());
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .build();
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirmUser(@RequestParam String token) throws IOException {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElse(null);

        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            if (verificationToken != null) {
                verificationTokenRepository.delete(verificationToken);
                userRepository.deleteById(verificationToken.getUser().getUserID());
            }
            // Đọc file HTML từ resource folder
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("templates/confirm-failed.html");
            if (inputStream == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("File xác nhận thất bại không tìm thấy");
            }
            String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_HTML)
                    .body(html);
        }

        User user = verificationToken.getUser();
        user.setActive(true);
        userRepository.save(user);
        verificationTokenRepository.delete(verificationToken);

        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("templates/confirm-success.html");
        if (inputStream == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File xác nhận thành công không tìm thấy");
        }
        String html = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(html);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authenticationService.forgotPassword(email);
        return ResponseEntity.ok("Email đặt lại mật khẩu đã được gửi");
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token) {
        authenticationService.resetPassword(token);
        return ResponseEntity.ok("Mật khẩu mới đã được gửi qua email của bạn.");
    }



    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
}
