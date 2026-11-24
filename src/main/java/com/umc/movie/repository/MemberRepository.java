package com.umc.movie.repository;

import com.umc.movie.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 해당 닉네임을 가진 회원이 존재하는지 확인
    boolean existsByNickname(String nickname);

    // 해당 이메일을 가진 회원이 존재하는지 확인
    boolean existsByEmail(String email);

    //이메일로 회원 정보로 조회(로그인할 때 사용)
    Optional<Member> findByEmail(String email);

    //자기 자신을 제외한 다른 회원의 닉네임 중복 확인
    boolean existsByNicknameAndMemberIdNot(String nickname, Long memberId);
}