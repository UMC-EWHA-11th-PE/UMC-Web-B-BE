package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.MemberInfoResponse;
import com.umc.movie.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.umc.movie.dto.MemberUpdateRequest;
import jakarta.validation.Valid;

@RestController // HTTP 요청을 받고 JSON으로 응답
@RequiredArgsConstructor // MemberService 생성자 주입
public class MyPageController {

    private final MemberService memberService;

    // GET /me
    // JWT로 인증된 회원의 정보 조회
    @GetMapping("/me")
    public ApiResponse<MemberInfoResponse> getMyInfo(
            @RequestAttribute("memberId") Long memberId
    ) {
        // 1. 인터셉터가 JWT에서 추출한 회원 ID를 전달받음
        MemberInfoResponse response = memberService.getMyInfo(memberId);

        // 2. 조회한 회원 정보를 반환
        return ApiResponse.ok("내 정보 조회에 성공했습니다.", response);

    }
    // PATCH /me
// JWT로 인증된 회원의 닉네임 수정
    @PatchMapping("/me")
    public ApiResponse<MemberInfoResponse> updateMyInfo(
            @RequestAttribute("memberId") Long memberId,
            @Valid @RequestBody MemberUpdateRequest request
    ) {
        // 1. JWT에서 추출한 회원 ID와 수정 요청 데이터를 Service에 전달
        MemberInfoResponse response = memberService.updateMyInfo(memberId, request);

        // 2. 수정된 회원 정보 반환
        return ApiResponse.ok("내 정보 수정에 성공했습니다.", response);
    }

}