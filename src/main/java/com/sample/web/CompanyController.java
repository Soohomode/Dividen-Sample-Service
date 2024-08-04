package com.sample.web;

import com.sample.model.Company;
import com.sample.model.constants.CacheKey;
import com.sample.persist.entity.CompanyEntity;
import com.sample.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company") // 경로에 공통되는 부분은 RequestMapping 으로
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CacheManager redisCacheManager;

    // 배당금 조회시 자동완성 기능 api
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        var result = this.companyService.getCompanyNamesByKeyword(keyword);

        return ResponseEntity.ok(result);
    }

    // 회사 리스트를 조회하는 api
    @GetMapping
    @PreAuthorize("hasRole('READ')") // 읽기 권한 있는 사용자만 접근 권한
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
    @PreAuthorize("hasRole('WRITE')") // 쓰기(관리자) 권한 있는 사용자만 접근 권한
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
    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')") // 쓰기(관리자) 권한 있는 사용자만 접근 권한
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = this.companyService.deleteCompany(ticker);
        // 캐시데이터도 삭제
        this.clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    public void clearFinanceCache(String companyName) {
        this.redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
    }

}
