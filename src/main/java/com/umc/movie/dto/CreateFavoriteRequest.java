package com.umc.movie.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// 즐겨찾기 등록 시 프론트엔드에서 전달받는 DTO
public record CreateFavoriteRequest(

        // 영화 ID는 필수이며 양수여야 함
        @NotNull
        @Positive
        Long movieId

) {
}