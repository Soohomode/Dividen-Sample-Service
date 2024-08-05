package com.sample.scheduler;

import com.sample.model.Company;
import com.sample.model.ScrapedResult;
import com.sample.model.constants.CacheKey;
import com.sample.persist.CompanyRepository;
import com.sample.persist.DividendRepository;
import com.sample.persist.entity.CompanyEntity;
import com.sample.persist.entity.DividendEntity;
import com.sample.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Scraper yahooFinanceScraper;

    // 일정 주기마다 수행
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true) // 캐시 비움
    @Scheduled(cron = "${scheduler.scrap.yahoo}")
    public void yahooFinanceScheduling() {
        log.info("scraping scheduler is started");
        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (var companyEntity : companies) {
            log.info("scraping scheduler is started => " + companyEntity.getName());
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(
                            new Company(companyEntity.getTicker(), companyEntity.getName()));

            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    // 디비든 모델을 디비든 엔티티로 매핑
                    .map(e -> new DividendEntity(companyEntity.getId(), e))
                    // 엘리먼트를 하나씩 디비든 레포지토리에 삽입 (존재 하지 않는다면)
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exists) { // 존재 하지 않는다면,
                            this.dividendRepository.save(e);
                            log.info("insert new dividend => " + e.toString());
                        }
                    });

            // 스크래핑 할 서버에 부하가 가지않도록 (연속적으로 서버에 요청을 날리지 않도록 일시정지)
            try {
                Thread.sleep(3000); // 3 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }

    }

}
