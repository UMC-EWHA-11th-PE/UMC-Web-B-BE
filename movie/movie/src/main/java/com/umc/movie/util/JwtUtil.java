package com.umc.movie.util;


import com.umc.movie.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final Duration accessExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-expiration}") Long accessExpiration
    ) {
        // 문자열 비밀키를 HMAC-SHA 서명용 키 객체로 바꿉니다.
        // 이 키가 있어야만 서명을 만들 수 있고 같은 키로만 검증할 수 있습니다.
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessExpiration = Duration.ofSeconds(accessExpiration);
    }

    //로그인에 성공한 회원에게 발급할 access token 만들기
    public String createAccessToke(Long memberId, String email){
        Instant now= Instant.now();

        return Jwts.builder()
                .subject(String.valueOf(memberId)) //토큰의 주인
                .claim("email", email) //참고용 부가 정보
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessExpiration)))
                .signWith(secretKey)
                .compact();
    }
    //토큰을 검증, 그 안에 담긴 memberId 꺼내기
    //서명이 안맞거나 만료된 토큰이면 예외를 던진다.
    public Long getMemberId(String token){
        try{
            return Long.valueOf(getClaims(token).getPayload().getSubject());
        }catch(ExpiredJwtException e){
            //만료, 사용자가 다시 로그인하면 해결되는 상황이라서 따로 구분한다.
            throw new ApiException(HttpStatus.UNAUTHORIZED, "AUTH401_4","토큰이 만료되었습니다. 다시 로그인해주세요");
        }catch(JwtException|IllegalArgumentException e){
            //서명 불일지, 형식 오류 등 모든 실패
            throw new ApiException(HttpStatus.UNAUTHORIZED, "AUTH401_3","유효하지 않은 토큰입니다.");
        }
    }

    //JWT 토큰이 유효한지 검증하고, 토큰에 저장된 정보(Claims)를 추출하는 메서드
    private Jws<Claims> getClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token);
    }



}


