package com.sample;

import com.sample.model.Company;
import com.sample.model.ScrapedResult;
import com.sample.scraper.Scraper;
import com.sample.scraper.YahooFinanceScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SampleApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SampleApplication.class, args);

		System.out.println(System.currentTimeMillis());

		Scraper scraper = new YahooFinanceScraper();
//		ScrapedResult result = scraper.scrap(Company.builder()
//				.ticker("O")
//				.build());
		var result = scraper.scrapCompanyByTicker("MMM");

		System.out.println(result);


//		try {
//			Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history/?frequency=1mo&period1=99153000&period2=1649030400");
//			Document document = connection.get();
//
//			Elements eles = document.getElementsByClass("table yf-ewueuo");
//			Element ele = eles.get(0); // table 전체
//
//			Element tbody = ele.children().get(1);// thead 는 0, tfoot 은 2 우리는 tbody
//			for (Element e : tbody.children()) {
//				String txt = e.text();
//				if (!txt.endsWith("Dividend")) { //Dividend 으로 끝나는 텍스트가 아니라면 넘김
//					continue;
//				}
//				// Jan 27, 2022 0.25 Dividend <- 이런 형태를 자르기 위해
//				String[] splits = txt.split(" "); // 공백 기준으로 잘라서 담음
//				String month = splits[0];
//				int day = Integer.valueOf(splits[1].replace(",", "")); // 27, 의 , 를 제거
//				int year = Integer.valueOf(splits[2]);
//				String dividend = splits[3];
//
//				System.out.println(year + "/" + month + "/" + day + " -> " + dividend);
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
