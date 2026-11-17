// src/main/java/com/umc/movie/repository/MemberRepository.java
package com.umc.movie.repository;

import com.umc.movie.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> origin/미로/main

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
<<<<<<< HEAD

    Optional<Member> findByEmail(String email);

    boolean existsByNicknameAndMemberIdNot(String nickname, Long memberId);
=======
>>>>>>> origin/미로/main
}