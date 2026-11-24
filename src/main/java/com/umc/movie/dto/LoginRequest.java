
package com.umc.movie.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// 9주차: Swagger 요청 데이터 설명 및 예시
import io.swagger.v3.oas.annotations.media.Schema;

// 로그인할 때 클라이언트가 서버로 보내는 데이터
public record LoginRequest(

        @Schema(description = "가입할 때 사용한 이메일", example = "challenger@umc.com")
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        String email,

        @Schema(description = "가입할 때 사용한 비밀번호", example = "Umc1234!")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password

) {
}
