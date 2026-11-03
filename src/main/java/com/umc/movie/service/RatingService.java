package com.umc.movie.service;

import com.umc.movie.dto.RatingResponse;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service // 영화 평점 조회 기능을 처리하는 Service
@RequiredArgsConstructor // Repository를 주입받는 생성자 자동 생성
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;

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
}