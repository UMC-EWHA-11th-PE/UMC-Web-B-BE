package com.umc.movie.util;

import com.umc.movie.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration}") long accessExpiration
    ) {
        // 환경변수의 Base64 문자열을 실제 비밀키로 변환
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessExpiration = accessExpiration;
    }

    // 1. 로그인 성공 시 JWT 발급
    public String createAccessToken(Long memberId, String email) {
        Date now = new Date();

        return Jwts.builder()
                .subject(String.valueOf(memberId)) // 회원 ID
                .claim("email", email)               // 회원 이메일
                .issuedAt(now)                       // 발급 시간
                .expiration(new Date(
                        now.getTime() + accessExpiration * 1000
                ))                                   // 만료 시간
                .signWith(secretKey)                  // 비밀키로 서명
                .compact();                          // JWT 문자열 생성
    }

    // 2. JWT가 유효한지 확인하고 회원 ID 추출
    public Long getMemberId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey) // 서명 검증
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Long.valueOf(claims.getSubject());

        } catch (ExpiredJwtException e) {
            // 만료된 토큰
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_4",
                    "만료된 토큰입니다."
            );

        } catch (JwtException | IllegalArgumentException e) {
            // 위조되었거나 형식이 잘못된 토큰
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "AUTH401_3",
                    "유효하지 않은 토큰입니다."
            );
        }
    }
}