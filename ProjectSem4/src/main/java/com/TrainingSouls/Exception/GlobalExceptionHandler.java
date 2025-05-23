package com.TrainingSouls.Exception;

import com.TrainingSouls.DTO.Response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Lỗi do hệ thống tự định nghĩa (Application level)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException e) {
        log.error(e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getErrorCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    // 2. Truy cập trái phép (Spring Security)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDenied(AccessDeniedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(403)
                .body(ApiResponse.builder()
                        .code(1006)
                        .message("Bạn không có quyền truy cập tài nguyên này")
                        .build());
    }

    // 3. Dữ liệu truyền vào DTO không hợp lệ (Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(1007)
                .message("Dữ liệu không hợp lệ: " + message)
                .build());
    }

    // 4. Enum hoặc kiểu dữ liệu sai trong query/path variable
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse> handleEnumMismatch(MethodArgumentTypeMismatchException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(1008)
                .message("Tham số không hợp lệ: " + ex.getName() + ". Giá trị truyền vào sai kiểu hoặc không nằm trong danh sách cho phép.")
                .build());
    }

    // 5. Không tìm thấy endpoint hoặc request mapping
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(NoHandlerFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                .code(1009)
                .message("Không tìm thấy đường dẫn: " + ex.getRequestURL())
                .build());
    }

    // 6. JSON không parse được (ví dụ enum sai hoặc body không đúng định dạng)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleUnreadable(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(1010)
                .message("Dữ liệu JSON không hợp lệ hoặc sai định dạng.")
                .build());
    }

    // 7. Lỗi truyền thiếu tham số bắt buộc
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .code(1011)
                .message("Thiếu tham số bắt buộc: " + ex.getParameterName())
                .build());
    }

    // 8. Lỗi không hỗ trợ phương thức HTTP (GET thay vì POST chẳng hạn)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ApiResponse.builder()
                .code(1012)
                .message("Phương thức HTTP không được hỗ trợ: " + ex.getMethod())
                .build());
    }

    // 9. Lỗi SQL (nếu muốn bắt ở đây, nên cẩn thận vì không phải lỗi nào cũng nên show)
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse> handleSQLException(SQLException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                .code(1013)
                .message("Lỗi cơ sở dữ liệu: " + ex.getMessage())
                .build());
    }

    // 10. Lỗi null pointer (có thể log nội bộ, không nên lộ thông tin)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse> handleNullPointer(NullPointerException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                .code(1014)
                .message("Có lỗi hệ thống xảy ra (NullPointerException). Vui lòng thử lại.")
                .build());
    }

    // 11. Lỗi chung (cái gì chưa bắt thì rơi vào đây)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleOtherException(Exception ex) {
        log.error(ex.getMessage());
        ex.printStackTrace(); // log cho dev
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                .code(9999)
                .message("Lỗi không xác định. Vui lòng liên hệ quản trị viên.")
                .build());
    }

}

