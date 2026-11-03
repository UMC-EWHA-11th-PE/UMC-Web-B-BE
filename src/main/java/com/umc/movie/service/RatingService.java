package com.umc.movie.service;

import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.dto.UpdateRatingRequest;
import com.umc.movie.entity.Member;
import com.umc.movie.entity.Rating;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // 영화 평점 조회 및 등록·수정·삭제 기능을 처리하는 Service
@RequiredArgsConstructor // Repository를 주입받는 생성자 자동 생성
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;

    // ==================== 5주차: 평점 조회 ====================

    // 특정 회원이 작성한 평점 목록 조회
    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByMember(Long memberId) {

        // 회원이 존재하지 않으면 404 오류 반환
        if (!memberRepository.existsById(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "존재하지 않는 유저입니다."
            );
        }

        // 해당 회원의 평점을 최신순으로 조회하고 DTO로 변환
        return ratingRepository
                .findByMember_MemberIdOrderByRatingIdDesc(memberId)
                .stream()
                .map(RatingResponse::from)
                .toList();
    }

    // ==================== 6주차: 평점 등록 ====================

    // 새로운 영화 평점 등록
    @Transactional
    public Long create(Long memberId, CreateRatingRequest request) {

        // 1. 회원이 존재하는지 확인 (없으면 404)
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 유저입니다."
                ));

        // 2. 동일한 영화에 이미 평점을 등록했는지 확인 (중복이면 409)
        if (ratingRepository.existsByMember_MemberIdAndMovieId(
                memberId, request.movieId())) {
            throw new IllegalArgumentException(
                    "이미 이 영화에 평점을 남기셨습니다."
            );
        }

        // 3. 새로운 Rating 객체 생성
        Rating rating = Rating.builder()
                .member(member)
                .movieId(request.movieId())
                .score(request.score())
                .comment(request.comment())
                .build();

        // 4. DB에 저장하고 생성된 평점 ID 반환
        return ratingRepository.save(rating).getRatingId();
    }

    // ==================== 6주차: 평점 수정 ====================

    // 기존 평점과 후기 수정
    @Transactional
    public void update(Long ratingId, Long memberId, UpdateRatingRequest request) {

        // 1. 평점 존재 여부 및 작성자 확인
        Rating rating = findOwnedRating(ratingId, memberId);

        // 2. 별점과 후기 변경
        rating.update(request.score(), request.comment());

        // 3. 변경된 평점 저장
        ratingRepository.save(rating);
    }

    // ==================== 6주차: 평점 삭제 ====================

    // 기존 평점 삭제
    @Transactional
    public void delete(Long ratingId, Long memberId) {

        // 1. 평점 존재 여부 및 작성자 확인
        Rating rating = findOwnedRating(ratingId, memberId);

        // 2. DB에서 평점 삭제
        ratingRepository.delete(rating);
    }

    // ==================== 6주차: 소유권 검증 ====================

    // 평점이 존재하는지, 요청한 회원이 작성자인지 확인하는 공통 메서드
    private Rating findOwnedRating(Long ratingId, Long memberId) {

        // 1. 평점이 존재하지 않으면 404 반환
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "존재하지 않는 평점입니다."
                ));

        // 2. 평점 작성자와 요청한 회원이 다르면 403 반환
        if (!rating.getMember().getMemberId().equals(memberId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "본인이 작성한 평점만 수정하거나 삭제할 수 있습니다."
            );
        }

        // 3. 검증을 통과한 평점 반환
        return rating;
    }
}