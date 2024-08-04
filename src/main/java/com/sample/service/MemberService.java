package com.sample.service;

import com.sample.exception.impl.AlreadyExistUserException;
import com.sample.model.Auth;
import com.sample.persist.entity.MemberEntity;
import com.sample.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder; // 인코딩된 패스워드
    private final MemberRepository memberRepository;

    // username 으로 유저 로드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("couldn't find user => " + username));
    }

    // 회원가입 메서드
    public MemberEntity register(Auth.SignUp member) {
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new AlreadyExistUserException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        MemberEntity result = this.memberRepository.save(member.toEntity());

        return result;
    }

    // 로그인시 검증 메서드
    public MemberEntity authenticate(Auth.SignIn member) {

        MemberEntity user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));
        // 인코딩 안된 패스워드와 인코딩된 패스워드 매칭
        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

}
