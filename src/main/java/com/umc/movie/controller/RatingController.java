package com.umc.movie.controller;

import com.umc.movie.dto.RatingResponse;
import com.umc.movie.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // HTTP 요청을 받아 JSON으로 응답
@RequiredArgsConstructor // RatingService 의존성 주입
@RequestMapping("/members") // 공통 URL 설정
public class RatingController {

    private final RatingService ratingService;

    // GET /members/{memberId}/ratings
    // 특정 회원이 작성한 영화 평점 목록 조회
    @GetMapping("/{memberId}/ratings")
    public List<RatingResponse> getMemberRatings(@PathVariable Long memberId) {

        // Service를 호출하여 해당 회원의 평점 목록 반환
        return ratingService.getRatingsByMember(memberId);
    }
}