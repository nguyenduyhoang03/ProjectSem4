package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.AuthenticationRequest;
import com.TrainingSouls.DTO.Request.IntrospectRequest;
import com.TrainingSouls.DTO.Response.AuthenticationResponse;
import com.TrainingSouls.DTO.Response.IntrospectResponse;
import com.TrainingSouls.Entity.InvalidatedToken;
import com.TrainingSouls.Entity.PasswordResetToken;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.InvalidatedTokenRepository;
import com.TrainingSouls.Repository.PasswordResetTokenRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordResetTokenRepository passwordResetTokenRepository;
    EmailService emailService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new AppException(ErrorCode.UNAUTHENTICATED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        boolean isActive = user.getActive();
        if (!authenticated || !isActive) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .success(true)
                .build();

    }

    public void logout(IntrospectRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());

        String jit = signToken.getJWTClaimsSet().getJWTID();

        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        String token = UUID.randomUUID().toString();

        // Nếu đã có token trước đó, cập nhật lại
        PasswordResetToken resetToken = passwordResetTokenRepository.findByUser(user)
                .orElse(new PasswordResetToken());

        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // 30 phút hết hạn
        passwordResetTokenRepository.save(resetToken);

        String resetURL = "http://54.251.220.228:8080/trainingSouls/auth/reset-password?token=" + token;
        String content = """
        <html>
          <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
            <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
              <h2 style="color: #333333;">Yêu cầu đặt lại mật khẩu</h2>
              <p style="font-size: 16px; color: #555555;">
                Bạn vừa gửi yêu cầu đặt lại mật khẩu cho tài khoản TrainingSouls.
                Nhấn vào nút bên dưới để đặt lại mật khẩu:
              </p>
              <div style="text-align: center; margin: 30px 0;">
                <a href="%s" style="background-color: #FF5722; color: white; padding: 12px 24px; text-decoration: none; font-size: 16px; border-radius: 6px; display: inline-block;">
                  Đặt lại mật khẩu
                </a>
              </div>
              <p style="font-size: 14px; color: #888888;">
                Nếu bạn không yêu cầu, vui lòng bỏ qua email này.
              </p>
            </div>
          </body>
        </html>
        """.formatted(resetURL);

        emailService.sendHtmlEmail(user.getEmail(), "Yêu cầu đặt lại mật khẩu - TrainingSouls", content);
    }

    public void resetPassword(String token) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        // Tạo mật khẩu mới
        String newPassword = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Xóa token sau khi sử dụng
        passwordResetTokenRepository.delete(resetToken);

        // Gửi email chứa mật khẩu mới
        String content = String.format("""
            <html>
              <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                  <h2 style="color: #333333;">Mật khẩu mới của bạn</h2>
                  <p style="font-size: 16px; color: #555555;">
                    Mật khẩu mới của bạn là: <strong>%s</strong>
                  </p>
                  <p style="font-size: 14px; color: #888888;">
                    Vui lòng đăng nhập và đổi mật khẩu ngay sau khi đăng nhập để đảm bảo an toàn.
                  </p>
                </div>
              </body>
            </html>
            """, newPassword);


        emailService.sendHtmlEmail(user.getEmail(), "Mật khẩu mới - TrainingSouls", content);
    }



    public AuthenticationResponse refreshAccessToken(String oldToken) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(oldToken);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        boolean isVerified = signedJWT.verify(verifier);
        if (!isVerified) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Kiểm tra token còn hạn không
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expiration.before(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Check nếu token đã bị thu hồi (logout)
        String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
        if (invalidatedTokenRepository.existsById(jwtId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Lấy thông tin user
        String email = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        // Tạo token mới
        String newToken = generateToken(user);

        return AuthenticationResponse.builder()
                .token(newToken)
                .success(true)
                .build();
    }



    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified = signedJWT.verify(verifier);

        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!(verified && expirationDate.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("NGUYEN DUY HOANG")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(3, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("userId", user.getUserID())
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));
        return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("cannot generate token", e);
        }
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;
        try {
            verifyToken(token);
        }catch (AppException e){
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();

    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
               if (!CollectionUtils.isEmpty(role.getPermissions()))
                role.getPermissions()
                        .forEach(permission -> {stringJoiner.add(permission.getName());});
            });

        }

        return stringJoiner.toString();
    }
}
