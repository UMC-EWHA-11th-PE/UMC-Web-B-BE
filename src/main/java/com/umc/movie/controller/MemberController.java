package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.AvailabilityResponse;
import com.umc.movie.dto.MemberResponse;
import com.umc.movie.dto.SignUpRequest;
import com.umc.movie.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // HTTP 요청을 받고 JSON으로 응답하는 컨트롤러
@RequiredArgsConstructor // MemberService를 주입받는 생성자 자동 생성
@RequestMapping("/members") // 이 컨트롤러의 공통 URL
public class MemberController {

    private final MemberService memberService;

    // GET /members/nickname/{nickname}
    // 닉네임 중복 확인 API
    @GetMapping("/nickname/{nickname}")
    public AvailabilityResponse checkNickname(@PathVariable String nickname) {
        return memberService.checkNickname(nickname);
    }

    // GET /members/email/{email}
    // 이메일 중복 확인 API
    @GetMapping("/email/{email}")
    public AvailabilityResponse checkEmail(@PathVariable String email) {
        return memberService.checkEmail(email);
    }

    // POST /members
    // 회원가입 API
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> signUp(
            @Valid @RequestBody SignUpRequest request
    ) {
        // Service에서 회원가입 처리 후 결과 반환
        MemberResponse response = memberService.signUp(request);

        // 회원 생성 성공 시 HTTP 201 Created 반환
        return ResponseEntity.status(201)
                .body(ApiResponse.created("회원가입에 성공했습니다.", response));
    }
}