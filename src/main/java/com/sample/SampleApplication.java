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
		SpringApplication.run(SampleApplication.class, args);

//		Scraper scraper = new YahooFinanceScraper();
////		ScrapedResult result = scraper.scrap(Company.builder()
////				.ticker("O")
////				.build());

//		var result = scraper.scrapCompanyByTicker("MMM");
//		System.out.println(result);

	}

}
