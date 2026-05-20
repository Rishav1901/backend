package com.infy.instagram.trendingposts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.infy.instagram.trendingposts.config.TrendingExternalProperties;


@SpringBootApplication
@EnableCaching
@EnableScheduling
@ConfigurationPropertiesScan(basePackageClasses = TrendingExternalProperties.class)
public class TrendingpostsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrendingpostsApplication.class, args);
	}

}
