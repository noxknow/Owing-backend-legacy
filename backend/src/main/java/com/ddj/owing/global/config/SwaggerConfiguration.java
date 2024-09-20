package com.ddj.owing.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 설정 클래스
 * Swagger 를 사용하여 API 명세서를 자동으로 생성하며, API 보안 설정을 정의
 */
@OpenAPIDefinition(
        info = @Info(
                title = "Owing api 명세서",
                description = "<h3>Owing API Reference for Developers</h3>",
                version = "v1",
                contact = @Contact(
                        name = "Owing",
                        email = "letsowing@gmail.com",
                        url = ""
                )
        )
)

@Configuration

public class SwaggerConfiguration {

}
