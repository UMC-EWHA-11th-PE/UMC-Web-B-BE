package com.umc.movie.dto;

import com.umc.movie.entity.Favorite;

// 즐겨찾기 영화 정보를 프론트엔드에 전달하는 DTO
public record FavoriteResponse(
        Long movieId
) {

    // Favorite 엔티티를 FavoriteResponse DTO로 변환
    public static FavoriteResponse from(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getMovieId()
        );
    }
}