//package com.TrainingSouls.Exception;
//
//import com.TrainingSouls.DTO.Response.ApiResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handlingException(RuntimeException e) {
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getErrorCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//
//
//    @ExceptionHandler(value = AppException.class)
//    ResponseEntity<ApiResponse> handlingAppException(AppException e) {
//        ErrorCode errorCode = e.getErrorCode();
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(errorCode.getErrorCode());
//        apiResponse.setMessage(errorCode.getMessage());
//        return ResponseEntity
//                .status(errorCode.getStatusCode())
//                .body(apiResponse);
//    }
//
//    @ExceptionHandler(value = AccessDeniedException.class)
//    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException e) {
//        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
//
//        return ResponseEntity.status(errorCode.getStatusCode()).body(
//                ApiResponse.builder()
//                        .code(errorCode.getErrorCode())
//                        .message(errorCode.getMessage())
//                        .build()
//        );
//    }
//
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        String enumKey = e.getFieldError().getDefaultMessage();
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//        try {
//            errorCode = ErrorCode.valueOf(enumKey);
//        }catch (IllegalArgumentException iae) {
//            iae.printStackTrace();
//        }
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(errorCode.getErrorCode());
//        apiResponse.setMessage(errorCode.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//
//
//}
