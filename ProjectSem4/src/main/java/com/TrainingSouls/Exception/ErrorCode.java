package com.TrainingSouls.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    NAME_INVALID(1002, "Name must be 10 characters and maximum 50 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be 8 characters and maximum 100 characters",HttpStatus.BAD_REQUEST),
    INVALID_KEY(1004, "Invalid Message", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Not Found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
    ;


    private final int errorCode;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int errorCode, String message, HttpStatusCode statusCode) {
        this.errorCode = errorCode;
        this.message = message;
        this.statusCode = statusCode;
    }


}
