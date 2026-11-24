package com.umc.movie.service;

import com.umc.movie.dto.CreateMovieRecordRequest;
import com.umc.movie.dto.FavoriteResponse;
import com.umc.movie.dto.MovieRecordResponse;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.entity.Favorite;
import com.umc.movie.entity.Member;
import com.umc.movie.entity.Rating;
import com.umc.movie.exception.ApiException;
import com.umc.movie.repository.FavoriteRepository;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieRecordService {

    private final MemberRepository memberRepository;
    private final RatingRepository ratingRepository;
    private final FavoriteRepository favoriteRepository;

    // ==================== 8주차: 영화 기록 저장 ====================

    // 평점과 즐겨찾기 저장을 하나의 트랜잭션으로 처리
    @Transactional
    public MovieRecordResponse create(
            Long memberId, CreateMovieRecordRequest request) {

        // 1. JWT에서 추출한 회원 ID로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "MEMBER404_1",
                        "존재하지 않는 회원입니다."
                ));

        // 2. 이미 평점을 등록한 영화인지 확인
        if (ratingRepository.existsByMember_MemberIdAndMovieId(
                memberId, request.movieId())) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "RATING409_1",
                    "이미 이 영화에 평점을 남기셨습니다."
            );
        }

        // 3. 즐겨찾기 추가 요청이 있다면 중복 등록 여부 확인
        if (request.addToFavorites()
                && favoriteRepository.existsByMember_MemberIdAndMovieId(
                memberId, request.movieId())) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "FAVORITE409_1",
                    "이미 즐겨찾기에 등록된 영화입니다."
            );
        }

        // 4. 새로운 평점 객체 생성 및 저장
        Rating rating = Rating.builder()
                .member(member)
                .movieId(request.movieId())
                .score(request.score())
                .comment(request.comment())
                .build();

        Rating savedRating = ratingRepository.save(rating);

        // 5. 즐겨찾기 추가 요청이 true일 경우에만 저장
        FavoriteResponse favoriteResponse = null;

        if (request.addToFavorites()) {

            Favorite favorite = Favorite.builder()
                    .member(member)
                    .movieId(request.movieId())
                    .build();

            Favorite savedFavorite = favoriteRepository.save(favorite);

            favoriteResponse = FavoriteResponse.from(savedFavorite);
        }

        // 6. 평점과 즐겨찾기 저장 결과 반환
        return new MovieRecordResponse(
                RatingResponse.from(savedRating),
                favoriteResponse
        );
    }
}