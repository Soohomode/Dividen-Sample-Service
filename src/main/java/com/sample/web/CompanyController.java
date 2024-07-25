package com.sample.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company") // 경로에 공통되는 부분은 RequestMapping 으로
public class CompanyController {

    // 배당금 조회시 자동완성 기능 api
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    // 회사 리스트를 조회하는 api
    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    // 회사 저장 api
    @PostMapping
    public ResponseEntity<?> addCompany() {
        return null;
    }

    // 회사 삭제 api
    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }

}
