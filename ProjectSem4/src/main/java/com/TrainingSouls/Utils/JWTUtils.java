package com.TrainingSouls.Utils;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;

public class JWTUtils {
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    // Ngăn chặn khởi tạo object của class
    private JWTUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Long getSubjectFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null) {
            throw new RuntimeException("JWT Token not found in request");
        }

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getLongClaim("userId");
        } catch (ParseException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    private static String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}

