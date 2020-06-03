package com.kalaha.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for swagger documentation
 * @author Praveen
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * API bean for Docket object.
	 *
	 * @return the docket
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
				.build().apiInfo(apiInfo());
	}

	/**
	 * ApiInfo object which contains REST API basic info.
	 *
	 * @return the api info
	 */
	private ApiInfo apiInfo() {
		return new ApiInfo("Kalaha REST Api", "Rest API for Kalaha game operations", "v1",
				"",
				new Contact("Praveen", "None", "praveen.itian@gmail.com"), "None", "None", Collections.emptyList());

	}
}
