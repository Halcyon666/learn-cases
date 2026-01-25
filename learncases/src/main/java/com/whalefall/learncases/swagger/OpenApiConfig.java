package com.whalefall.learncases.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    /**
     * http://localhost:8080/swagger-ui/index.html<br>
     * swagger url
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("我的 Spring Boot 3.4 API 文档")
                        .version("1.0")
                        .description("这是一个基于 Spring Boot 3.4.1 和 SpringDoc 的示例工程")
                        .contact(new Contact()
                                .name("开发团队")
                                .email("dev@example.com")));
    }

}
