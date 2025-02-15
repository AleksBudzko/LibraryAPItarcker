package com.booktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.booktracker"})
public class BookTrackerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookTrackerServiceApplication.class, args);
	}
}
