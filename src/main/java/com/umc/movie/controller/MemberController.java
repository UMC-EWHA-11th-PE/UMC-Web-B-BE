package com.umc.movie.controller;

import com.umc.movie.dto.AvailabilityResponse;
import com.umc.movie.service.MemberService;
import lombok.RequiredArgsConstructor;
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
}