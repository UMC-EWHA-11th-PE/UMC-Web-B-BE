
package com.umc.movie.repository;

import com.umc.movie.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);

    // 이메일 중복 확인
    boolean existsByEmail(String email);

    // 이메일로 회원 조회 (로그인)
    Optional<Member> findByEmail(String email);

    // 본인을 제외한 닉네임 중복 확인 (마이페이지 수정)
    boolean existsByNicknameAndMemberIdNot(String nickname, Long memberId);
}
