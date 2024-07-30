package com.sample.web;

import com.sample.model.Company;
import com.sample.persist.entity.CompanyEntity;
import com.sample.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company") // 경로에 공통되는 부분은 RequestMapping 으로
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 배당금 조회시 자동완성 기능 api
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        var result = this.companyService.autocomplete(keyword);

        return ResponseEntity.ok(result);
    }

    // 회사 리스트를 조회하는 api
    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    /**
     * 회사 및 배당금 정보 추가
     * @param request
     * @return
     */

    // 회사 저장 api
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();// trim 앞뒤 공백 제거
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = this.companyService.save(ticker);
        this.companyService.addAutocompleteKeyword(company.getName()); // 자동완성 trie 에도 회사명 저장

        return ResponseEntity.ok(company);
    }

    // 회사 삭제 api
    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }

}
