package com.apress.todo;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TodoJpaApplication {

	// inject via application.properties
	private PropertiesConfiguration app;

	@Autowired
	public void setApp(PropertiesConfiguration app) {
	    this.app = app;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(TodoJpaApplication.class, args);
	}
	
	/**
	 * Limit output from http://localhost:8080/swagger-ui.html#/ to just this API vs all Spring stuff
	 * @return
	 */
	@Bean
	public Docket SwaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.paths(PathSelectors.ant("/api/*"))
				.apis(RequestHandlerSelectors.basePackage("com.apress.todo"))
				.build()
				.apiInfo(apiDetails())
				;
	}
	
	private ApiInfo apiDetails() {
		
		ApiInfo apiInfo = new ApiInfo(
				"ToDo API",
				"Sample API for managing ToDos",
				"1.0",
				"Free to use",
				new springfox.documentation.service.Contact("Rod Madden", "http://localhost:8080/api/todo", "ramrod1460@hotmail.com"),
				"API License",
				"http://localhost:8080/api/todo",
				Collections.emptyList()
				);
		return apiInfo;
	}
}
