package com.umc.movie.service;

import com.umc.movie.dto.AvailabilityResponse;
import com.umc.movie.dto.MemberResponse;
import com.umc.movie.dto.SignUpRequest;
import com.umc.movie.entity.Member;
import com.umc.movie.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 이 클래스가 비즈니스 로직을 담당하는 Service임을 표시
@RequiredArgsConstructor // final 필드를 초기화하는 생성자 자동 생성
public class MemberService {

    // 회원 데이터를 조회하거나 저장하기 위해 Repository 사용
    private final MemberRepository memberRepository;

    // 비밀번호를 bcrypt로 해시하기 위한 객체
    private final PasswordEncoder passwordEncoder;

    // 닉네임 사용 가능 여부 확인 (5주차)
    @Transactional(readOnly = true)
    public AvailabilityResponse checkNickname(String nickname) {

        // 해당 닉네임이 이미 존재하는지 확인
        boolean exists = memberRepository.existsByNickname(nickname);

        // 존재하면 false, 존재하지 않으면 true 반환
        return new AvailabilityResponse(!exists);
    }

    // 이메일 사용 가능 여부 확인 (5주차)
    @Transactional(readOnly = true)
    public AvailabilityResponse checkEmail(String email) {

        // 해당 이메일이 이미 존재하는지 확인
        boolean exists = memberRepository.existsByEmail(email);

        // 존재하면 false, 존재하지 않으면 true 반환
        return new AvailabilityResponse(!exists);
    }

    // 회원가입 처리 (6주차)
    @Transactional
    public MemberResponse signUp(SignUpRequest request) {

        // 1. 이메일 중복 확인
        if (memberRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 2. 닉네임 중복 확인
        if (memberRepository.existsByNickname(request.nickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 3. 비밀번호를 bcrypt로 해시하고 Member 객체 생성
        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .build();

        // 4. Repository를 통해 DB에 회원 정보 저장
        Member savedMember = memberRepository.save(member);

        // 5. 비밀번호를 제외한 회원 정보만 반환
        return new MemberResponse(
                savedMember.getMemberId(),
                savedMember.getEmail(),
                savedMember.getNickname()
        );
    }
}