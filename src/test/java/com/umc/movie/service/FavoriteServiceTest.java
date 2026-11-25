
package com.umc.movie.service;

import com.umc.movie.dto.CreateFavoriteRequest;
import com.umc.movie.dto.FavoriteResponse;
import com.umc.movie.entity.Favorite;
import com.umc.movie.entity.Member;
import com.umc.movie.exception.ApiException;
import com.umc.movie.repository.FavoriteRepository;
import com.umc.movie.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// Spring 서버나 실제 DB 없이 Mockito를 이용해 단위 테스트 실행
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    // 실제 Repository 대신 가짜 객체 생성
    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    // 가짜 Repository를 FavoriteService에 주입
    @InjectMocks
    private FavoriteService favoriteService;

    // ==================== 즐겨찾기 등록 성공 ====================

    @Test
    @DisplayName("즐겨찾기를 정상적으로 등록한다")
    void createFavorite() {

        // given: 테스트에 필요한 데이터와 조건 설정
        Member member = Member.builder()
                .email("test@umc.com")
                .password("encodedPassword")
                .nickname("테스트회원")
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        // 아직 즐겨찾기에 등록되지 않은 영화
        given(favoriteRepository.existsByMember_MemberIdAndMovieId(1L, 550L))
                .willReturn(false);

        given(favoriteRepository.save(any(Favorite.class)))
                .willReturn(Favorite.builder()
                        .member(member)
                        .movieId(550L)
                        .build());

        // when: 실제 테스트할 메서드 실행
        FavoriteResponse response =
                favoriteService.create(1L, new CreateFavoriteRequest(550L));

        // then: 반환 결과와 저장 여부 검증
        assertThat(response.movieId()).isEqualTo(550L);

        verify(favoriteRepository).save(any(Favorite.class));
    }

    // ==================== 즐겨찾기 중복 등록 실패 ====================

    @Test
    @DisplayName("이미 등록된 영화는 즐겨찾기에 중복 등록할 수 없다")
    void createFavoriteWhenDuplicated() {

        // given: 테스트 회원 생성
        Member member = Member.builder()
                .email("test@umc.com")
                .password("encodedPassword")
                .nickname("테스트회원")
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        // 이미 즐겨찾기에 등록된 영화
        given(favoriteRepository.existsByMember_MemberIdAndMovieId(1L, 550L))
                .willReturn(true);

        // when & then: 중복 등록 시 예외가 발생하는지 검증
        assertThatThrownBy(() ->
                favoriteService.create(1L, new CreateFavoriteRequest(550L)))
                .isInstanceOf(ApiException.class)
                .hasMessage("이미 즐겨찾기에 등록된 영화입니다.");

        // 중복 등록이므로 저장 메서드가 호출되면 안 됨
        verify(favoriteRepository, never()).save(any(Favorite.class));
    }
}
