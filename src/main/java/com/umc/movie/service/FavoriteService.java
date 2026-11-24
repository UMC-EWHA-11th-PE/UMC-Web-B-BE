package com.umc.movie.service;

import com.umc.movie.dto.CreateFavoriteRequest;
import com.umc.movie.dto.FavoriteResponse;
import com.umc.movie.dto.FavoriteStatusResponse;
import com.umc.movie.entity.Favorite;
import com.umc.movie.entity.Member;
import com.umc.movie.exception.ApiException;
import com.umc.movie.repository.FavoriteRepository;
import com.umc.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;

    // ==================== 즐겨찾기 목록 조회 ====================

    // 로그인한 회원의 전체 즐겨찾기 목록 조회
    public List<FavoriteResponse> getFavorites(Long memberId) {

        return favoriteRepository
                .findByMember_MemberIdOrderByFavoriteIdDesc(memberId)
                .stream()
                .map(FavoriteResponse::from)
                .toList();
    }

    // ==================== 즐겨찾기 여부 조회 ====================

    // 로그인한 회원이 해당 영화를 즐겨찾기에 등록했는지 확인
    public FavoriteStatusResponse isFavorite(Long memberId, Long movieId) {

        boolean isFavorite = favoriteRepository
                .existsByMember_MemberIdAndMovieId(memberId, movieId);

        return new FavoriteStatusResponse(isFavorite);
    }

    // ==================== 즐겨찾기 등록 ====================

    @Transactional
    public FavoriteResponse create(Long memberId, CreateFavoriteRequest request) {

        // JWT에서 추출한 회원 ID로 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "MEMBER404_1",
                        "존재하지 않는 회원입니다."
                ));

        // 이미 등록된 영화인지 확인
        if (favoriteRepository.existsByMember_MemberIdAndMovieId(
                memberId, request.movieId())) {

            throw new ApiException(
                    HttpStatus.CONFLICT,
                    "FAVORITE409_1",
                    "이미 즐겨찾기에 등록된 영화입니다."
            );
        }

        // 새로운 즐겨찾기 객체 생성
        Favorite favorite = Favorite.builder()
                .member(member)
                .movieId(request.movieId())
                .build();

        // MySQL에 즐겨찾기 정보 저장 후 응답 DTO 반환
        return FavoriteResponse.from(favoriteRepository.save(favorite));
    }

    // ==================== 즐겨찾기 삭제 ====================

    @Transactional
    public void delete(Long memberId, Long movieId) {

        // 로그인한 회원의 즐겨찾기 정보만 조회
        Favorite favorite = favoriteRepository
                .findByMember_MemberIdAndMovieId(memberId, movieId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND,
                        "FAVORITE404_1",
                        "즐겨찾기에 등록되지 않은 영화입니다."
                ));

        // 즐겨찾기 삭제
        favoriteRepository.delete(favorite);
    }
}