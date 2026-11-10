package com.umc.movie.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    // HTTP 상태 코드 (401, 403, 404 등)
    private final HttpStatus status;

    // 서비스에서 사용하는 에러 코드
    private final String code;

    public ApiException(HttpStatus status, String code, String message) {
        super(message); // 부모 클래스에 에러 메시지 전달
        this.status = status;
        this.code = code;
    }
}