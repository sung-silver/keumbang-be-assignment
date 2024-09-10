package com.keumbang.resource.common.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
    info =
        @Info(
            title = "keumbang-resource API Documentation",
            description = "keumbang-resource API Documentation",
            version = "v1"))
@Configuration
public class SwaggerConfig {}
