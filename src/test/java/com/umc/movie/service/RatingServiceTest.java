package com.umc.movie.service;

import com.umc.movie.dto.CreateRatingRequest;
import com.umc.movie.dto.UpdateRatingRequest;
import com.umc.movie.entity.Member;
import com.umc.movie.entity.Rating;
import com.umc.movie.exception.ApiException;
import com.umc.movie.repository.MemberRepository;
import com.umc.movie.repository.RatingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// 실제 서버와 DB 없이 Mockito를 사용한 단위 테스트
@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private RatingService ratingService;

    // ==================== 평점 중복 등록 실패 ====================

    @Test
    @DisplayName("같은 영화에 평점을 중복 등록하면 409와 RATING409_1이 발생한다")
    void createRatingWhenDuplicated() {

        // given: 테스트 회원 생성
        Member member = Member.builder()
                .email("rating@umc.com")
                .password("encodedPassword")
                .nickname("평점테스트회원")
                .build();

        given(memberRepository.findById(1L))
                .willReturn(Optional.of(member));

        // 이미 해당 영화에 평점을 등록한 상태
        given(ratingRepository
                .existsByMember_MemberIdAndMovieId(1L, 550L))
                .willReturn(true);

        CreateRatingRequest request =
                new CreateRatingRequest(550L, 5, "재미있는 영화");

        // when & then: 중복 등록 예외의 상태 코드와 code 검증
        assertThatThrownBy(() -> ratingService.create(1L, request))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getStatus())
                            .isEqualTo(HttpStatus.CONFLICT);

                    assertThat(exception.getCode())
                            .isEqualTo("RATING409_1");
                });

        // 중복 등록이므로 저장하면 안 됨
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    // ==================== 다른 회원의 평점 수정 실패 ====================

    @Test
    @DisplayName("다른 회원의 평점을 수정하면 403과 RATING403_1이 발생한다")
    void updateRatingByOtherMember() {

        // given: 평점 작성자의 회원 ID는 1번
        Member owner = mock(Member.class);

        given(owner.getMemberId()).willReturn(1L);

        // 1번 회원이 작성한 평점
        Rating rating = Rating.builder()
                .member(owner)
                .movieId(550L)
                .score(5)
                .comment("기존 후기")
                .build();

        given(ratingRepository.findById(10L))
                .willReturn(Optional.of(rating));

        UpdateRatingRequest request =
                new UpdateRatingRequest(3, "수정된 후기");

        // when & then: 2번 회원이 1번 회원의 평점을 수정
        assertThatThrownBy(() ->
                ratingService.update(10L, 2L, request))
                .isInstanceOfSatisfying(ApiException.class, exception -> {
                    assertThat(exception.getStatus())
                            .isEqualTo(HttpStatus.FORBIDDEN);

                    assertThat(exception.getCode())
                            .isEqualTo("RATING403_1");
                });

        // 수정 권한이 없으므로 저장하면 안 됨
        verify(ratingRepository, never()).save(any(Rating.class));

        // 기존 평점과 후기가 유지되는지 확인
        assertThat(rating.getScore()).isEqualTo(5);
        assertThat(rating.getComment()).isEqualTo("기존 후기");
    }
}