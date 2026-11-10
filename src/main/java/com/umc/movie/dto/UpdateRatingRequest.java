package com.umc.movie.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 기존 영화 평점을 수정할 때 사용하는 요청 DTO
public record UpdateRatingRequest(

        // 수정할 별점은 필수이며 1~5점 사이여야 함
        @NotNull(message = "별점을 입력해주세요.")
        @Min(value = 1, message = "별점은 1점 이상이어야 합니다.")
        @Max(value = 5, message = "별점은 5점 이하여야 합니다.")
        Integer score,

        // 후기는 선택 사항이며 최대 500자
        @Size(max = 500, message = "후기는 500자 이하로 입력해주세요.")
        String comment

) {}