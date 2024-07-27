package com.sample.scraper;

import com.sample.model.Company;
import com.sample.model.Dividend;
import com.sample.model.ScrapedResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YahooFinanceScraper {

    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history/?frequency=1mo&period1=%d&period2=%d";

    // 스크랩동작을 수행할수 있는 메서드
    public ScrapedResult scrap(Company company) { // company 를 받고 ScrapedResult 리턴
        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try {
            long start = 0;
            long end = 0;
            String url = String.format(STATISTICS_URL, company.getTicker(), start, end);

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
                String month = splits[0];
                int day = Integer.valueOf(splits[1].replace(",", "")); // 27, 의 , 를 제거
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

//                System.out.println(year + "/" + month + "/" + day + " -> " + dividend);

            }
            scrapResult.setDividendEntities(dividends);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return scrapResult;
    }

}
