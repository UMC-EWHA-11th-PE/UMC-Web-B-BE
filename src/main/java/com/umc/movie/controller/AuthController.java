package com.umc.movie.controller;

import com.umc.movie.dto.ApiResponse;
import com.umc.movie.dto.LoginRequest;
import com.umc.movie.dto.LoginResponse;
import com.umc.movie.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 9주차: Swagger 문서화
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "로그인과 토큰 발급 API")
public class AuthController {

    private final AuthService authService;

    // POST /auth/login : 로그인 요청 처리
    @Operation(
            summary = "이메일 로그인",
            description = """
                    이메일과 비밀번호로 로그인하고 Access Token을 발급받습니다.
                    발급받은 토큰은 위쪽 Authorize 버튼에 넣어두면 인증이 필요한 API에 자동으로 실립니다.
                    """
    )
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        // 로그인 검증 후 JWT를 발급받아 응답
        LoginResponse response = authService.login(request);

        return ApiResponse.ok("로그인에 성공했습니다.", response);
    }
}