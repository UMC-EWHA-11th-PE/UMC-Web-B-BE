package com.umc.movie.dto;

// 평점과 즐겨찾기 저장 결과를 함께 반환하는 DTO
public record MovieRecordResponse(
        RatingResponse rating,
        FavoriteResponse favorite
) {}