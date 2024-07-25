package com.sample.persist.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "COMPANY") // 테이블명
@Getter
@ToString
@NoArgsConstructor // args 가 없는 생성자를 만들어주는 어노테이션
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // 중복방지
    private String ticker;

    private String name;

}
