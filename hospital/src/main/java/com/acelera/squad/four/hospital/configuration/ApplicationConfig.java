package com.acelera.squad.four.hospital.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.mongodb.MongoClient;

import feign.Logger;

@Configuration
@ComponentScan(basePackages = "com.acelera.squad.four.hospital")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableMongoRepositories({ "com.acelera.squad.four.hospital.repositories" })
public class ApplicationConfig {
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
	
    @Bean
    public MongoClient mongo() {
        return new MongoClient("localhost");
    }
 
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), "test");
    }

}
