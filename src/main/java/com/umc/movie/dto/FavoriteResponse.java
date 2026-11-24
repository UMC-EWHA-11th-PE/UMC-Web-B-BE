package com.umc.movie.dto;

import com.umc.movie.entity.Favorite;

import java.time.LocalDateTime;

public record FavoriteResponse(
        Long favoriteId,
        Long movieId,
        LocalDateTime createdAt
) {
    // Favorite 엔티티를 응답 DTO로 변환
    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getFavoriteId(),
                favorite.getMovieId(),
                favorite.getCreatedAt()
        );
    }
}