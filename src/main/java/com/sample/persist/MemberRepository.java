package com.sample.persist;

import com.sample.persist.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // Id를 기준으로 회원 정보를 찾는 메서드
    Optional<MemberEntity> findByUsername(String username);

    // 회원가입시 회원정보가 존재하는지 체크
    boolean existsByUsername(String username);
}
