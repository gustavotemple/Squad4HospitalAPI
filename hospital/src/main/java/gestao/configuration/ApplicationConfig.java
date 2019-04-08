package gestao.configuration;

import java.util.Collections;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Predicates;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import feign.Logger;
import gestao.models.Hospital;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = { ApplicationConfig.BASE_PACKAGE, "controllers", "service" })
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EnableMongoRepositories({ ApplicationConfig.BASE_PACKAGE + ".repositories" })
public class ApplicationConfig {

	public static final String HOSPITAIS = "hospitais";
	public static final String BASE_URL = "/v1/" + HOSPITAIS;
	public static final String BASE_PACKAGE = "gestao";

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
		MongoClient mongoClient = new MongoClient("localhost");
		MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
		MongoCollection<Hospital> hospitals = mongoDatabase.getCollection(HOSPITAIS, Hospital.class);
		hospitals.createIndex(Indexes.geo2dsphere("localizacao"));
		return mongoClient;
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongo(), "test");
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE)).paths(PathSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/error.*"))).build().apiInfo(apiInfo());
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Hospital API by Squad 4", "Hospital API by Squad 4", "1.0", "", ApiInfo.DEFAULT_CONTACT,
				"MIT License", "https://mit-license.org", Collections.emptyList());
	}

}
