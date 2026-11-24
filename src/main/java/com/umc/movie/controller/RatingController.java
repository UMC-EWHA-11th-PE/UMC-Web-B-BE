
package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.dto.RatingResponse;
import com.umc.movie.dto.UpdateRatingRequest;
import com.umc.movie.service.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // HTTP 요청을 받아 JSON으로 응답
@RequiredArgsConstructor // RatingService 의존성 주입
public class RatingController {

    private final RatingService ratingService;

    // ==================== 5주차: 평점 조회 ====================

    // GET /members/{memberId}/ratings
    // 특정 회원이 작성한 영화 평점 목록 조회
    @GetMapping("/members/{memberId}/ratings")
    public List<RatingResponse> getMemberRatings(@PathVariable Long memberId) {

        // 기존 5주차 평점 조회 기능 유지
        return ratingService.getRatingsByMember(memberId);
    }

    // ==================== 7주차: JWT 기반 평점 등록 ====================

    // POST /ratings
    // 로그인한 회원의 새로운 영화 평점 등록
    @PostMapping("/ratings")
    public ResponseEntity<ApiResponse<Long>> createRating(
            @RequestAttribute("memberId") Long memberId,
            @Valid @RequestBody CreateRatingRequest request) {

        // JWT에서 추출한 회원 ID로 평점 등록
        Long ratingId = ratingService.create(memberId, request);

        // 등록 성공 시 201 Created 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("평점 등록에 성공했습니다.", ratingId));
    }

    // ==================== 7주차: JWT 기반 평점 수정 ====================

    // PATCH /ratings/{ratingId}
    // 로그인한 회원이 본인의 평점 수정
    @PatchMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> updateRating(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request) {

        // JWT 회원 ID를 기준으로 작성자 권한 확인 후 수정
        ratingService.update(ratingId, memberId, request);

        // 수정 성공 시 200 OK 반환
        return ResponseEntity
                .ok(ApiResponse.<Void>ok("평점 수정에 성공했습니다.", null));
    }

    // ==================== 7주차: JWT 기반 평점 삭제 ====================

    // DELETE /ratings/{ratingId}
    // 로그인한 회원이 본인의 평점 삭제
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<Void> deleteRating(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable Long ratingId) {

        // JWT 회원 ID를 기준으로 작성자 권한 확인 후 삭제
        ratingService.delete(ratingId, memberId);

        // 기존 6주차와 동일하게 204 No Content 반환
        return ResponseEntity.noContent().build();
    }
}
