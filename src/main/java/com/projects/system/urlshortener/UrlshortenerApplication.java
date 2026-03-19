package com.projects.system.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableMongoRepositories(basePackages = {"com.projects.system.urlshortener.repository"})
@EnableCaching
@EnableAsync
public class UrlshortenerApplication {
	public static void main(String[] args) {
		SpringApplication.run(UrlshortenerApplication.class, args);
	}
}
