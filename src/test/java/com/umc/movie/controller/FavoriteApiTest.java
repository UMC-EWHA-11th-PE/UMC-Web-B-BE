
package com.umc.movie.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.movie.dto.CreateFavoriteRequest;
import com.umc.movie.entity.Favorite;
import com.umc.movie.entity.Member;
import com.umc.movie.repository.FavoriteRepository;
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
class FavoriteApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Member member;
    private String authorization;

    // 각 테스트 실행 전 테스트 회원 및 JWT 생성
    @BeforeEach
    void setUp() {

        // H2에서 회원 생성 및 수정 시간의 기본값 설정
        jdbcTemplate.execute(
                "ALTER TABLE member ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP"
        );

        jdbcTemplate.execute(
                "ALTER TABLE member ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP"
        );

        member = memberRepository.save(Member.builder()
                .email("test@umc.com")
                .password("encodedPassword")
                .nickname("테스트회원")
                .build());

        // 실제 JwtUtil을 사용해 테스트용 토큰 발급
        authorization = "Bearer " + jwtUtil.createAccessToken(
                member.getMemberId(),
                member.getEmail()
        );
    }

    // ==================== 인증 실패 테스트 ====================

    @Test
    @DisplayName("토큰 없이 즐겨찾기를 등록하면 401을 반환한다")
    void createFavoriteWithoutToken() throws Exception {

        mockMvc.perform(post("/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateFavoriteRequest(550L))))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("AUTH401_2"));
    }

    // ==================== 즐겨찾기 등록 성공 ====================

    @Test
    @DisplayName("유효한 토큰으로 즐겨찾기를 등록하면 201을 반환한다")
    void createFavorite() throws Exception {

        mockMvc.perform(post("/favorites")
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateFavoriteRequest(550L))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value("COMMON201"))
                .andExpect(jsonPath("$.result.movieId").value(550));
    }

    // ==================== 즐겨찾기 중복 등록 실패 ====================

    @Test
    @DisplayName("이미 등록된 영화를 다시 등록하면 409를 반환한다")
    void createFavoriteWhenDuplicated() throws Exception {

        // 해당 영화를 미리 즐겨찾기에 등록
        favoriteRepository.save(Favorite.builder()
                .member(member)
                .movieId(550L)
                .build());

        mockMvc.perform(post("/favorites")
                        .header("Authorization", authorization)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new CreateFavoriteRequest(550L))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.isSuccess").value(false))
                .andExpect(jsonPath("$.code").value("FAVORITE409_1"));
    }

    // ==================== 즐겨찾기 여부 조회 ====================

    @Test
    @DisplayName("등록하지 않은 영화의 즐겨찾기 여부는 false이다")
    void getFavoriteStatus() throws Exception {

        mockMvc.perform(get("/favorites/550")
                        .header("Authorization", authorization))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.isFavorite").value(false));
    }

    // ==================== Swagger 공개 접근 테스트 ====================

    @Test
    @DisplayName("토큰 없이 Swagger API 문서에 접근할 수 있다")
    void getApiDocsWithoutToken() throws Exception {

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}
