package com.TrainingSouls.Exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Error"),
    USER_EXISTED(1001, "User existed"),
    NAME_INVALID(1002, "Name must be 10 characters and maximum 50 characters"),
    PASSWORD_INVALID(1003, "Password must be 8 characters and maximum 100 characters"),
    INVALID_KEY(1004, "Invalid Message"),
    NOT_FOUND(404, "Not Found"),
    EMAIL_OR_PASSWORD_INCORRECT(406, "Invalid Email Or Password"),
    ;


    private int errorCode;
    private String message;

    ErrorCode(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
