package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.MemberInfoResponse;
import com.umc.movie.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.umc.movie.dto.MemberUpdateRequest;
import jakarta.validation.Valid;

// 9주차: Swagger 문서화
import com.umc.movie.config.SwaggerConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController // HTTP 요청을 받고 JSON으로 응답
@RequiredArgsConstructor // MemberService 생성자 주입
@Tag(name = "마이페이지", description = "내 정보 조회 및 수정 API")
@SecurityRequirement(name = SwaggerConfig.BEARER_AUTH)
public class MyPageController {

    private final MemberService memberService;

    // GET /me
    // JWT로 인증된 회원의 정보 조회
    @Operation(
            summary = "내 정보 조회",
            description = "로그인한 회원의 정보를 조회합니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 정보 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 회원 (MEMBER404_1)"
            )
    })
    @GetMapping("/me")
    public ApiResponse<MemberInfoResponse> getMyInfo(
            @Parameter(hidden = true)
            @RequestAttribute("memberId") Long memberId
    ) {
        // 1. 인터셉터가 JWT에서 추출한 회원 ID를 전달받음
        MemberInfoResponse response = memberService.getMyInfo(memberId);

        // 2. 조회한 회원 정보를 반환
        return ApiResponse.ok("내 정보 조회에 성공했습니다.", response);
    }

    // PATCH /me
    // JWT로 인증된 회원의 닉네임 수정
    @Operation(
            summary = "내 정보 수정",
            description = "로그인한 회원의 닉네임을 수정합니다. JWT 토큰이 필요합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "내 정보 수정 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "토큰 누락(AUTH401_2), 유효하지 않은 토큰(AUTH401_3), 만료된 토큰(AUTH401_4)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 회원 (MEMBER404_1)"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "이미 사용 중인 닉네임 (MEMBER409_1)"
            )
    })
    @PatchMapping("/me")
    public ApiResponse<MemberInfoResponse> updateMyInfo(
            @Parameter(hidden = true)
            @RequestAttribute("memberId") Long memberId,
            @Valid @RequestBody MemberUpdateRequest request
    ) {
        // 1. JWT에서 추출한 회원 ID와 수정 요청 데이터를 Service에 전달
        MemberInfoResponse response = memberService.updateMyInfo(memberId, request);

        // 2. 수정된 회원 정보 반환
        return ApiResponse.ok("내 정보 수정에 성공했습니다.", response);
    }
}