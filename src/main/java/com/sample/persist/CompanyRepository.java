package com.sample.persist;

import com.sample.persist.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> { // <레포지토리가 활용하게될 엔티티, 엔티티의 PK 의 타입>
    boolean existsByTicker(String ticker);

    Optional<CompanyEntity> findByName(String name); // 회사 name

    // like 연산자 JPA 사용
    Page<CompanyEntity> findByNameStartingWithIgnoreCase(String s, Pageable pageable); // IgnoreCase => 대소문자 구분 X
}
