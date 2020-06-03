package com.kalaha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.kalaha.repository.GameRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Spring boot started application.
 * @author Praveen
 * 
 */
@SpringBootApplication
@EnableSwagger2
@EnableMongoRepositories(basePackageClasses = GameRepository.class)
public class KalahaApiApplication extends SpringBootServletInitializer {

	/**
	 * The application starts from here.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(KalahaApiApplication.class, args);
	}

}
