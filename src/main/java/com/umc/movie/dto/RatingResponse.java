package com.umc.movie.dto;

import com.umc.movie.entity.Rating;
import java.time.LocalDateTime;

// 영화 평점 정보를 프론트엔드에 전달하는 DTO
public record RatingResponse(
        Long ratingId,
        Long movieId,
        Integer score,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    // Rating 엔티티를 RatingResponse DTO로 변환
    public static RatingResponse from(Rating rating) {
        return new RatingResponse(
                rating.getRatingId(),
                rating.getMovieId(),
                rating.getScore(),
                rating.getComment(),
                rating.getCreatedAt(),
                rating.getUpdatedAt()
        );
    }
}