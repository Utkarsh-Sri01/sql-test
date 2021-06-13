package com.sql.test.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration

public class SwaggerConfig {

    /**
     * Swagger Configuration.
     * @return Swagger Docket.
     */
    @Bean
    public Docket sqlTestTypeApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("New sql type test api")
                .apiInfo(apiInfo()).select().paths(paths()).build();
    }

    private Predicate<String> paths() {
        return or(regex("/"), regex("/executeQuery"),regex("/startTest"),regex("/submitTest"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("New sql type test API")
                .description("API reference for developers")
                .version("1.0").build();
    }
}
