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

// 9주차: Swagger 문서화 및 JWT 인증 표시
import com.umc.movie.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "평점", description = "영화 평점 관련 API")
@RestController // HTTP 요청을 받아 JSON으로 응답
@RequiredArgsConstructor // RatingService 의존성 주입
public class RatingController {

    private final RatingService ratingService;

    // ==================== 5주차: 평점 조회 ====================

    // GET /members/{memberId}/ratings
    // 특정 회원이 작성한 영화 평점 목록 조회
    @Operation(
            summary = "회원별 평점 조회",
            description = "특정 회원이 작성한 영화 평점 목록을 최신순으로 조회합니다. JWT 토큰이 필요하지 않은 공개 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "회원별 평점 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 회원 (COMMON404)"
            )
    })
    @GetMapping("/members/{memberId}/ratings")
    public List<RatingResponse> getMemberRatings(
            @PathVariable Long memberId) {

        // 기존 5주차 평점 조회 기능 유지
        return ratingService.getRatingsByMember(memberId);
    }

    // ==================== 7주차: JWT 기반 평점 등록 ====================

    // POST /ratings
    // 로그인한 회원의 새로운 영화 평점 등록
    @Operation(
            summary = "평점 등록",
            description = "로그인한 회원이 영화에 평점과 후기를 등록합니다. JWT 토큰이 필요합니다."
    )
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "평점 등록 성공 (COMMON201)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 데이터 검증 실패 (COMMON4001)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 회원 (COMMON404)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "평점 중복 등록 (RATING409_1)"
            )
    })
    @PostMapping("/ratings")
    public ResponseEntity<ApiResponse<Long>> createRating(
            @Parameter(hidden = true)
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
    @Operation(
            summary = "평점 수정",
            description = "로그인한 회원이 본인의 평점과 후기를 수정합니다. JWT 토큰이 필요합니다."
    )
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "평점 수정 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 데이터 검증 실패 (COMMON4001)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "다른 회원의 평점 수정 불가 (RATING403_1)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 평점 (COMMON404)"
            )
    })
    @PatchMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> updateRating(
            @Parameter(hidden = true)
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
    @Operation(
            summary = "평점 삭제",
            description = "로그인한 회원이 본인의 평점을 삭제합니다. JWT 토큰이 필요합니다."
    )
    @SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "평점 삭제 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "다른 회원의 평점 삭제 불가 (RATING403_1)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 평점 (COMMON404)"
            )
    })
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<Void> deleteRating(
            @Parameter(hidden = true)
            @RequestAttribute("memberId") Long memberId,
            @PathVariable Long ratingId) {

        // JWT 회원 ID를 기준으로 작성자 권한 확인 후 삭제
        ratingService.delete(ratingId, memberId);

        // 기존 6주차와 동일하게 204 No Content 반환
        return ResponseEntity.noContent().build();
    }
}