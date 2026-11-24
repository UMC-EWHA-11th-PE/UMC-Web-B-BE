package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.CreateMovieRecordRequest;
import com.umc.movie.dto.MovieRecordResponse;
import com.umc.movie.service.MovieRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie-records")
public class MovieRecordController {

    private final MovieRecordService movieRecordService;

    // ==================== 8주차: 영화 기록 저장 ====================

    // POST /movie-records
    @PostMapping
    public ResponseEntity<ApiResponse<MovieRecordResponse>> create(
            @RequestAttribute("memberId") Long memberId,
            @Valid @RequestBody CreateMovieRecordRequest request) {

        // JWT 회원 ID를 기준으로 영화 기록 저장
        MovieRecordResponse response =
                movieRecordService.create(memberId, request);

        // 저장 성공 시 201 Created 반환
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(
                        "영화 기록을 저장했습니다.", response
                ));
    }
}