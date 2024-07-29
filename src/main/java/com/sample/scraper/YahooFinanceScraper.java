package com.sample.scraper;

import com.sample.model.Company;
import com.sample.model.Dividend;
import com.sample.model.ScrapedResult;
import com.sample.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";

    private static final long START_TIME = 86400; // 60 * 60 * 24 60초 60분 24시간

    @Override
    // 스크랩동작을 수행할수 있는 메서드
    public ScrapedResult scrap(Company company) { // company 를 받고 ScrapedResult 리턴
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try {
            long now = System.currentTimeMillis() / 1000; // 밀리세컨드로 현재의 시간을 가져온 값 (초단위로 바꾸기 위해 1000으로 나눔)

            String url = String.format(STATISTICS_URL, company.getTicker(), START_TIME, now);

            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByClass("table yf-ewueuo");
            Element tableEle = parsingDivs.get(0); // table 전체

            Element tbody = tableEle.children().get(1);// thead 는 0, tfoot 은 2 우리는 tbody

            List<Dividend> dividends = new ArrayList<>();

            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) { //Dividend 으로 끝나는 텍스트가 아니라면 넘김
                    continue;
                }

                // Jan 27, 2022 0.25 Dividend <- 이런 형태를 자르기 위해
                String[] splits = txt.split(" "); // 공백 기준으로 잘라서 담음
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", "")); // 27, 의 , 를 제거
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if (month < 0) { // 1~12 월 중 값이 아닌 다른 값이 오면 -1 을 리턴하기에 오류 발생
                    throw new RuntimeException("Unexpected Month enum value => " + splits[0]);
                }

                // 정상적으로 개월수를 받아온 경우
                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build());

//                System.out.println(year + "/" + month + "/" + day + " -> " + dividend);

            }
            scrapResult.setDividends(dividends);

        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }

        return scrapResult;
    }

    @Override
    // 회사의 ticker 를 받으면 회사의 메타정보를 스크래핑해서 반환
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();
            Element titleEle = document.getElementsByClass("yf-3a2v0c").get(0);
            // 깔끔하게 가져오기 위해 문자열 후처리
            String titleWithTicker = titleEle.text().trim();
            // 정규 표현식을 사용하여 회사명 뒤의 티커를 제거
            String title = titleWithTicker.replaceAll("\\s*\\([^\\)]*\\)$", "").trim();
            // abc - def - xyz => def
            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
