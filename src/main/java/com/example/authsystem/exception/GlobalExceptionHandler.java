package com.example.authsystem.exception;
import com.example.authsystem.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 1) Custom ApiException
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                400,
                "Bad Request",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(), 401, "Unauthorized",
                "Login required or token invalid",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(), 403, "Forbidden",
                "You don't have permission to access this resource",
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    // ✅ 2) Validation Error (@Valid fails)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {

        String msg = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .orElse("Validation failed");

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                400,
                "Validation Error",
                msg,
                req.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ✅ 3) Any other exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                500,
                "Internal Server Error",
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}