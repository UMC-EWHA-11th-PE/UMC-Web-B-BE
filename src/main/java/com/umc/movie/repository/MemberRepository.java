package com.umc.movie.repository;

import com.umc.movie.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 해당 닉네임을 가진 회원이 존재하는지 확인
    boolean existsByNickname(String nickname);

    // 해당 이메일을 가진 회원이 존재하는지 확인
    boolean existsByEmail(String email);
}