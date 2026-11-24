package com.umc.movie.dto;

// 특정 영화의 즐겨찾기 등록 여부를 반환하는 DTO
public record FavoriteStatusResponse(
        boolean isFavorite
) {
}