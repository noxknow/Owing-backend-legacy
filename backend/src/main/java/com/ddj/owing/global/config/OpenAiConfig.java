package com.ddj.owing.global.config;

import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;

    /**
     * OpenAI 이미지 API 를 위한 Bean 을 생성하는 메서드
     *
     * @return OpenAiImageApi 객체
     */
    @Bean
    public OpenAiImageApi openAiImageApi() {
        return new OpenAiImageApi(openAiApiKey);
    }
}
