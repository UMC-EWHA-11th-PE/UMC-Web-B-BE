package com.umc.movie.dto;

// 모든 API에서 공통으로 사용하는 응답 DTO
// T는 응답 데이터의 타입을 나타내는 제네릭 타입
public record ApiResponse<T>(
        boolean isSuccess,
        String code,
        String message,
        T result
) {

    // 데이터 생성 성공 시 사용 (HTTP 201)
    public static <T> ApiResponse<T> created(String message, T result) {
        return new ApiResponse<>(true, "COMMON201", message, result);
    }

    // 일반적인 요청 성공 시 사용 (HTTP 200)
    public static <T> ApiResponse<T> ok(String message, T result) {
        return new ApiResponse<>(true, "COMMON200", message, result);
    }

    // 요청 실패 시 사용
    public static ApiResponse<Void> failure(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}