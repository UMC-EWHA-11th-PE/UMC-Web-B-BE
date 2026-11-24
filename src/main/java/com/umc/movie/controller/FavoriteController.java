package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.CreateFavoriteRequest;
import com.umc.movie.dto.FavoriteResponse;
import com.umc.movie.dto.FavoriteStatusResponse;
import com.umc.movie.service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    // ==================== 즐겨찾기 목록 조회 ====================

    // GET /favorites
    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getFavorites(
            @RequestAttribute("memberId") Long memberId) {

        // 로그인한 회원의 즐겨찾기 목록 조회
        List<FavoriteResponse> favorites =
                favoriteService.getFavorites(memberId);

        return ResponseEntity.ok(
                ApiResponse.ok("즐겨찾기 목록 조회에 성공했습니다.", favorites)
        );
    }

    // ==================== 즐겨찾기 여부 조회 ====================

    // GET /favorites/{movieId}
    @GetMapping("/{movieId}")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> getFavorite(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable Long movieId) {

        // 해당 영화의 즐겨찾기 등록 여부 조회
        FavoriteStatusResponse response =
                favoriteService.isFavorite(memberId, movieId);

        return ResponseEntity.ok(
                ApiResponse.ok("즐겨찾기 여부 조회에 성공했습니다.", response)
        );
    }

    // ==================== 즐겨찾기 등록 ====================

    // POST /favorites
    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponse>> createFavorite(
            @RequestAttribute("memberId") Long memberId,
            @Valid @RequestBody CreateFavoriteRequest request) {

        // JWT 회원 ID를 기준으로 즐겨찾기 등록
        FavoriteResponse response =
                favoriteService.create(memberId, request);

        // 등록 성공 시 201 Created 및 등록 정보 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(
                        "즐겨찾기 등록에 성공했습니다.", response
                ));
    }

    // ==================== 즐겨찾기 삭제 ====================

    // DELETE /favorites/{movieId}
    @DeleteMapping("/{movieId}")
    public ResponseEntity<ApiResponse<Void>> deleteFavorite(
            @RequestAttribute("memberId") Long memberId,
            @PathVariable Long movieId) {

        // 로그인한 회원의 즐겨찾기 삭제
        favoriteService.delete(memberId, movieId);

        // 삭제 성공 시 200 OK 및 공통 응답 반환
        return ResponseEntity.ok(
                ApiResponse.<Void>ok("즐겨찾기를 해제했습니다.", null)
        );
    }
}