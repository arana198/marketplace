package com.marketplace.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    private static final String securitySchemaOAuth2 = "oauth2schema";
    private static final String authorizationScopeGlobal = "global";
    private static final String authorizationScopeGlobalDesc = "accessEverything";

    /*private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Study Buddy EmailSender")
                .description("This pending provides a JSON EmailSender for managing cricket club related data")
                .termsOfServiceUrl("http://springfox.io")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.marketplace"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(newArrayList(securitySchema()))
                .securityContexts(newArrayList(securityContext()));
    }

    private OAuth securitySchema() {
        AuthorizationScope authorizationScope = new AuthorizationScope(authorizationScopeGlobal, authorizationScopeGlobal);
        TokenEndpoint tokenEndpoint = new TokenEndpoint(swaggerOAuthUrl, "access_code");
        GrantType grantType = new AuthorizationCodeGrant(new TokenRequestEndpoint(swaggerOAuthUrl, oAuthClientId, oAuthClientSecret), tokenEndpoint);
        return new OAuth(securitySchemaOAuth2, newArrayList(authorizationScope), newArrayList(grantType));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope(authorizationScopeGlobal, authorizationScopeGlobalDesc);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(
                new SecurityReference(securitySchemaOAuth2, authorizationScopes));
    }*/

}
