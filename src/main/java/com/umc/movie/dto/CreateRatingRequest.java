package com.umc.movie.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

// 영화 평점 등록 요청 데이터를 전달하고 검증하는 DTO
public record CreateRatingRequest(

        // TMDB 영화 ID는 필수이며 양수여야 함
        @NotNull(message = "영화 ID를 입력해주세요.")
        @Positive(message = "영화 ID는 양수여야 합니다.")
        Long movieId,

        // 별점은 필수이며 1~5점 사이여야 함
        @NotNull(message = "별점을 입력해주세요.")
        @Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5점 이하여야 합니다.")
        Integer score,

        // 후기는 선택 사항이며 최대 500자
        @Size(max = 500, message = "후기는 500자 이하로 입력해주세요.")
        String comment

) {}