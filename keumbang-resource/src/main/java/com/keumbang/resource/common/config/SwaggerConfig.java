package com.keumbang.resource.common.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(
    info =
        @Info(
            title = "keumbang-resource API Documentation",
            description = "keumbang-resource API Documentation",
            version = "v1"))
@SecuritySchemes({
  @SecurityScheme(
      name = "Authorization",
      type = SecuritySchemeType.APIKEY,
      description = "access token",
      in = SecuritySchemeIn.HEADER,
      paramName = "Authorization"),
})
@Configuration
public class SwaggerConfig {}
