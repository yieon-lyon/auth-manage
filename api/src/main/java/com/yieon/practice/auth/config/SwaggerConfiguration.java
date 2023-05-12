package com.yieon.practice.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-29
 * <PRE>
 * com.yieon.practice.auth.config
 *     |SwaggerConfiguration.java
 * ------------------------
 * summary : swagger2 config
 * ------------------------
 * Revision history
 * 2023. 04. 29. yieon : Initial creation
 * </PRE>
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${security.oauth2.client.access-token-uri}")
    private String ACCESS_TOKEN_URI;

    @Value("#{'${security.oauth2.client.scope}'.trim().split(',')}")
    List<String> SCOPE;

    private static final String PATH_REGEX = "/(user|api).*";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.regex(PATH_REGEX))
            .build()
            .securitySchemes(Collections.singletonList(securityScheme()))
            .securityContexts(Collections.singletonList(securityContext()))
            .apiInfo(apiInfo());

    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
            .clientId("auth-server")
            .clientSecret("server")
            .useBasicAuthenticationWithAccessCodeGrant(false)
            .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(ACCESS_TOKEN_URI);

        SecurityScheme oauth = new OAuthBuilder().name("AUTH API")
            .grantTypes(Arrays.asList(grantType))
            .scopes(Arrays.asList(scopes()))
            .build();

        return oauth;
    }

    private AuthorizationScope[] scopes() {
        return SCOPE.stream().map(scope -> new AuthorizationScope(scope, "")).collect(Collectors.toList()).toArray(new AuthorizationScope[SCOPE.size()]);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
            .securityReferences(Arrays.asList(new SecurityReference("AUTH API", scopes())))
            .forPaths(PathSelectors.regex("/.*"))
            .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("AUTH resource-API server REST API")
            .description("resource-API server REST API")
            .license("Apache License Version 2.0")								// 라이선스 설정
            .licenseUrl("https://github.com/IBM-Bluemix/news-aggregator/blob/master/LICENSE") // 라이선스 URL1
            .version("1.0")
            .contact(new Contact("yieon", "none", "parrotbill@naver.com"))	//	연락처 설정
            .build();
    }

}
