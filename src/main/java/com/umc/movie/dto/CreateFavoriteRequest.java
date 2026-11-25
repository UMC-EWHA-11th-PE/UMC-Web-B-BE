
package com.umc.movie.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// 9주차: Swagger 요청 데이터 설명 및 예시
import io.swagger.v3.oas.annotations.media.Schema;

// 즐겨찾기 등록 시 프론트엔드에서 전달받는 DTO
public record CreateFavoriteRequest(

        // 영화 ID는 필수이며 양수여야 함
        @Schema(description = "TMDB 영화 ID", example = "550")
        @NotNull
        @Positive
        Long movieId

) {
}
