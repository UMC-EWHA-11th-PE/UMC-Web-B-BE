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
@RequestMapping("/members") // 공통 URL 설정
public class RatingController {

    private final RatingService ratingService;

    // ==================== 5주차: 평점 조회 ====================

    // GET /members/{memberId}/ratings
    // 특정 회원이 작성한 영화 평점 목록 조회
    @GetMapping("/{memberId}/ratings")
    public List<RatingResponse> getMemberRatings(@PathVariable Long memberId) {

        // Service를 호출하여 해당 회원의 평점 목록 반환
        return ratingService.getRatingsByMember(memberId);
    }

    // ==================== 6주차: 평점 등록 ====================

    // POST /members/{memberId}/ratings
    // 특정 회원의 새로운 영화 평점 등록
    @PostMapping("/{memberId}/ratings")
    public ResponseEntity<ApiResponse<Long>> createRating(
            @PathVariable Long memberId,
            @Valid @RequestBody CreateRatingRequest request) {

        // Service를 호출하여 평점을 저장하고 생성된 평점 ID 반환
        Long ratingId = ratingService.create(memberId, request);

        // 등록 성공 시 201 Created 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("평점 등록에 성공했습니다.", ratingId));
    }

    // ==================== 6주차: 평점 수정 ====================

    // PATCH /members/{memberId}/ratings/{ratingId}
    // 본인이 작성한 영화 평점 수정
    @PatchMapping("/{memberId}/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> updateRating(
            @PathVariable Long memberId,
            @PathVariable Long ratingId,
            @Valid @RequestBody UpdateRatingRequest request) {

        // Service를 호출하여 평점 수정
        ratingService.update(ratingId, memberId, request);

        // 수정 성공 시 200 OK 반환
        return ResponseEntity
                .ok(ApiResponse.<Void>ok("평점 수정에 성공했습니다.", null));
    }

    // ==================== 6주차: 평점 삭제 ====================

    // DELETE /members/{memberId}/ratings/{ratingId}
    // 본인이 작성한 영화 평점 삭제
    @DeleteMapping("/{memberId}/ratings/{ratingId}")
    public ResponseEntity<Void> deleteRating(
            @PathVariable Long memberId,
            @PathVariable Long ratingId) {

        // Service를 호출하여 평점 삭제
        ratingService.delete(ratingId, memberId);

        // 삭제 성공 시 204 No Content 반환 (응답 본문 없음)
        return ResponseEntity.noContent().build();
    }
}