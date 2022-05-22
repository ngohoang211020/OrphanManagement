package com.orphan.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicates;
import com.orphan.common.response.APIResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * The class for swagger configuration.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    private TypeResolver resolver;

    public SwaggerConfig(TypeResolver resolver) {
        this.resolver = resolver;
    }

    @Bean
    public Docket apiAll() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    }

    @Bean
    public Docket apiHrManager() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("Manager HR API").select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api.controller.manager.HR"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    }
    @Bean
    public Docket apiChildrenManager() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("Manager Children API").select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api.controller.manager.Children"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    } @Bean
    public Docket apiLogisticManager() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("Manager LOGISTIC API").select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api.controller.manager.Logistic"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    }

    @Bean
    public Docket apiAdmin() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("Admin API").select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api.controller.admin"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    }

    @Bean
    public Docket apiCommon() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("Common API").select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api.controller.common"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    }
    @Bean
    public Docket apiHome() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("Home API").select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api.controller.home"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    }
    @Bean
    public Docket apiProfileForUserLogin() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("User Logged Api").select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api.controller.profile"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    }

    @Bean
    public Docket apiProfileForEmployee() {
        List<ResponseMessage> responseMessages = Arrays.asList(
                new ResponseMessageBuilder().code(HttpStatus.BAD_REQUEST.value())
                        .message(HttpStatus.BAD_REQUEST.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.UNAUTHORIZED.value())
                        .message(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.FORBIDDEN.value())
                        .message(HttpStatus.FORBIDDEN.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.CONFLICT.value())
                        .message(HttpStatus.CONFLICT.getReasonPhrase()).build(),
                new ResponseMessageBuilder().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return new Docket(DocumentationType.SWAGGER_2).groupName("Employee Api").select()
                .apis(RequestHandlerSelectors.basePackage("com.orphan.api.controller.employee"))
                .paths(Predicates.not(PathSelectors.regex("/error.*"))).build()
                .genericModelSubstitutes(APIResponse.class).alternateTypeRules(
                        newRule(resolver.resolve(DeferredResult.class,
                                        resolver.resolve(APIResponse.class, WildcardType.class)),
                                resolver.resolve(WildcardType.class)))
                .apiInfo(metaData())
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.POST, responseMessages)
                .globalResponseMessage(RequestMethod.PUT, responseMessages)
                .globalResponseMessage(RequestMethod.GET, responseMessages)
                .globalResponseMessage(RequestMethod.DELETE, responseMessages);
    }
    private ApiInfo metaData() {
        return new ApiInfoBuilder().version("1.0").title("Orphan Management API")
                .description("Documentation for OrphanManagement API v1.0").build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Bearer", HttpHeaders.AUTHORIZATION, "header");
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
