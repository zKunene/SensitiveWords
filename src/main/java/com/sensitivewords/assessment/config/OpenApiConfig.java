package com.sensitivewords.assessment.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;

@Configuration
 public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer appInfo() {
        return openApi-> openApi.info(new Info().title("SensitiveWordsAPI").version("1.0").description("API to star out sensitive words"));
    }
 }