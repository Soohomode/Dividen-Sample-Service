package com.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

//@SpringBootApplication
//@EnableScheduling
public class SampleApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SampleApplication.class, args);

		for (int i = 0; i < 10; i++) {
			System.out.println("Hello -> " + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}

}
