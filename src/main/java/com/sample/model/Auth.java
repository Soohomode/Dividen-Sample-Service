package com.sample.model;

import com.sample.persist.entity.MemberEntity;
import lombok.Data;

import java.util.List;

public class Auth {

    @Data
    // 로그인 클래스
    public static class SignIn {
        private String username;
        private String password;
    }

    @Data
    // 회원가입 클래스
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles; // 어떤 권한을 가질지 (일반회원, 관리자)

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                                .username(this.username)
                                .password(this.password)
                                .roles(this.roles)
                                .build();
        }
    }
}
