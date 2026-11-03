package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

// 모든 Controller에서 발생하는 예외를 공통으로 처리
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. DTO 입력값 검증 실패 → 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(
            MethodArgumentNotValidException exception
    ) {
        // 검증에 실패한 첫 번째 필드의 오류 메시지 추출
        String message = exception.getBindingResult()
                .getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity.badRequest()
                .body(ApiResponse.failure("COMMON4001", message));
    }

    // 2. DB 제약 조건 위반 → 409 Conflict
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(
            DataIntegrityViolationException exception
    ) {
        return ResponseEntity.status(409)
                .body(ApiResponse.failure("COMMON4091", "이미 존재하는 데이터입니다."));
    }

    // 3. 이메일 또는 닉네임 중복 → 409 Conflict
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException exception
    ) {
        return ResponseEntity.status(409)
                .body(ApiResponse.failure("COMMON4092", exception.getMessage()));
    }

    // 4. HTTP 상태 코드가 지정된 예외 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatus(
            ResponseStatusException exception
    ) {
        int status = exception.getStatusCode().value();

        return ResponseEntity.status(status)
                .body(ApiResponse.failure("COMMON" + status, exception.getReason()));
    }
}