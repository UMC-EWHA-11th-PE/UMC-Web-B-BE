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

// 9주차: Swagger 문서화 및 JWT 인증 표시
import com.umc.movie.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
@Tag(name = "즐겨찾기", description = "로그인한 회원의 영화 즐겨찾기 API")
@SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
public class FavoriteController {

    private final FavoriteService favoriteService;

    // ==================== 즐겨찾기 목록 조회 ====================

    // GET /favorites
    @Operation(
            summary = "내 즐겨찾기 목록 조회",
            description = "로그인한 회원의 즐겨찾기 목록을 최신순으로 조회합니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 목록 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)"
            )
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<FavoriteResponse>>> getFavorites(
            @Parameter(hidden = true)
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
    @Operation(
            summary = "즐겨찾기 여부 조회",
            description = "로그인한 회원이 해당 영화를 즐겨찾기에 등록했는지 확인합니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 여부 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)"
            )
    })
    @GetMapping("/{movieId}")
    public ResponseEntity<ApiResponse<FavoriteStatusResponse>> getFavorite(
            @Parameter(hidden = true)
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
    @Operation(
            summary = "즐겨찾기 등록",
            description = "토큰의 주인인 회원이 TMDB 영화 ID 하나를 즐겨찾기에 추가합니다. JWT 토큰이 필요합니다."
    )
    // 9주차: 성공 및 실패 응답 문서화
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "즐겨찾기 등록 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": true,
                                      "code": "COMMON201",
                                      "message": "즐겨찾기 등록에 성공했습니다.",
                                      "result": {
                                        "favoriteId": 1,
                                        "movieId": 550,
                                        "createdAt": "2026-09-13T21:00:00"
                                      }
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "요청 데이터 검증 실패 (COMMON4001)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": false,
                                      "code": "AUTH401_2",
                                      "message": "인증 토큰이 필요합니다.",
                                      "result": null
                                    }
                                    """)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 회원 (MEMBER404_1)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 즐겨찾기한 영화 (FAVORITE409_1)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "isSuccess": false,
                                      "code": "FAVORITE409_1",
                                      "message": "이미 즐겨찾기에 등록된 영화입니다.",
                                      "result": null
                                    }
                                    """)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<FavoriteResponse>> createFavorite(
            @Parameter(hidden = true)
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
    @Operation(
            summary = "즐겨찾기 해제",
            description = "로그인한 회원이 해당 영화를 즐겨찾기에서 삭제합니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "즐겨찾기 해제 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "즐겨찾기에 등록되지 않은 영화 (FAVORITE404_1)"
            )
    })
    @DeleteMapping("/{movieId}")
    public ResponseEntity<ApiResponse<Void>> deleteFavorite(
            @Parameter(hidden = true)
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