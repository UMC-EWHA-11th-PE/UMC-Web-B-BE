package com.umc.movie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.entity.Member;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Spring 전체 환경을 불러오는 통합 테스트
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RatingApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String authorization;

    // 각 테스트 실행 전 테스트 환경 및 회원 설정
    @BeforeEach
    void setUp() {

        // H2에서 회원 생성 및 수정 시간의 기본값 설정
        jdbcTemplate.execute(
                "ALTER TABLE member ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP"
        );

        jdbcTemplate.execute(
                "ALTER TABLE member ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP"
        );

        // H2에서 평점 생성 및 수정 시간의 기본값 설정
        jdbcTemplate.execute(
                "ALTER TABLE rating ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP"
        );

        jdbcTemplate.execute(
                "ALTER TABLE rating ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP"
        );

        // 테스트용 회원 생성
        Member member = memberRepository.save(Member.builder()
                .email("rating@umc.com")
                .password("encodedPassword")
                .nickname("평점테스트회원")
                .build());

        // 실제 JwtUtil을 사용하여 테스트용 JWT 발급
        authorization = "Bearer " + jwtUtil.createAccessToken(
                member.getMemberId(),
                member.getEmail()
        );
    }

    // ==================== 인증 실패 테스트 ====================

    @Test
    @DisplayName("토큰 없이 평점을 등록하면 401과 AUTH401_2를 반환한다")
    void createRatingWithoutToken() throws Exception {

        // 평점 등록 요청 데이터 생성
        CreateRatingRequest request =
                new CreateRatingRequest(550L, 5, "재미있는 영화");

        // 토큰 없이 POST /ratings 요청
        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("AUTH401_2"));
    }

    // ==================== 평점 등록 성공 테스트 ====================

    @Test
    @DisplayName("유효한 토큰으로 평점을 등록하면 201과 COMMON201을 반환한다")
    void createRatingWithToken() throws Exception {

        // 평점 등록 요청 데이터 생성
        CreateRatingRequest request =
                new CreateRatingRequest(550L, 5, "재미있는 영화");

        // 유효한 JWT를 포함하여 POST /ratings 요청
        mockMvc.perform(post("/ratings")
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON201"))
                .andExpect(jsonPath("$.result").isNumber());
    }
}