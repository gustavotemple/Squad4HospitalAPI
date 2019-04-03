package gestao.configuration;

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
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;

import feign.Logger;
import gestao.models.Hospital;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = { "gestao", "controllers", "service" })
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@EnableMongoRepositories({ "gestao.repositories" })
public class ApplicationConfig {

	public static final String HOSPITAIS = "hospitais";

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
        return new Docket(DocumentationType.SWAGGER_2)
          .select()
          .apis(RequestHandlerSelectors.any())
          .paths(PathSelectors.any())
          .build();
    }

}
