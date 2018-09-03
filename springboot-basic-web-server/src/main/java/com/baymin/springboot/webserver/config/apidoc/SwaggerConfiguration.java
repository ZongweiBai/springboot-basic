package com.baymin.springboot.webserver.config.apidoc;

import com.fasterxml.classmate.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.schema.TypeNameExtractor;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@ConditionalOnClass({ ApiInfo.class, BeanValidatorPluginsConfiguration.class })
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfiguration {

	private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

	@Bean
	public Docket swaggerSpringfoxDocket() {
		log.debug("Starting Swagger");
		StopWatch watch = new StopWatch();
		watch.start();
		Contact contact = new Contact(
				"",
				"",
				"");

		ApiInfo apiInfo = new ApiInfo(
				"API Docs",
				"API documentation",
				"1.0.0",
				"",
				contact,
				"",
				"",
				new ArrayList<>());

		Docket docket = new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo)
				.forCodeGeneration(true)
				.directModelSubstitute(java.nio.ByteBuffer.class, String.class)
				.genericModelSubstitutes(ResponseEntity.class)
				.select()
				.paths(regex("/api/.*"))
				.build();
		watch.stop();
		log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
		return docket;
	}

	@Bean
	PageableParameterBuilderPlugin pageableParameterBuilderPlugin(TypeNameExtractor nameExtractor, TypeResolver resolver) {
		return new PageableParameterBuilderPlugin(nameExtractor, resolver);
	}
}