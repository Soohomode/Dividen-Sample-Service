package com.sample.web;

import com.sample.model.Auth;
import com.sample.persist.entity.MemberEntity;
import com.sample.security.TokenProvider;
import com.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController { // Auth = 인증

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        // 회원가입 API
        MemberEntity result = this.memberService.register(request);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        // 로그인 API
        MemberEntity member = this.memberService.authenticate(request);
        String token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());

        return ResponseEntity.ok(token);
    }

}
