package com.umc.movie.dto;

// 닉네임 또는 이메일의 사용 가능 여부를 전달하는 DTO
public record AvailabilityResponse(boolean available) {
}