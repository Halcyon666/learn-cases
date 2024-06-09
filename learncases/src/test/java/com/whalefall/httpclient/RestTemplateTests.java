package com.whalefall.httpclient;

import com.whalefall.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Halcyon
 * @date 2024/6/9 13:00
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class RestTemplateTests extends AbstractTest {

    private final Supplier<HttpHeaders> consumer1 = () -> {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));
        return headers;
    };
    private final Supplier<HttpHeaders> consumer2 = () -> {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("text/plain;charset=UTF-8"));
        return headers;
    };
    // this will failed
    private final Supplier<HttpHeaders> consumer3 = () -> {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("Accept-Charset", StandardCharsets.UTF_8.name());
        return headers;
    };
    private final Supplier<HttpHeaders> consumer4 = () -> {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Accept-Charset", "UTF-8");
        return headers;
    };
    private final Supplier<HttpHeaders> consumer5 = () -> {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "text/plain; charset=UTF-8");
        return headers;
    };
    @LocalServerPort
    private int port;

    public Stream<Arguments> getHeadersList() {
        return Stream.of(
                Arguments.of(consumer1.get(), true),
                Arguments.of(consumer2.get(), true),
                Arguments.of(consumer3.get(), false),
                Arguments.of(consumer4.get(), true),
                Arguments.of(consumer5.get(), true)
        );
    }

    @ParameterizedTest
    @MethodSource("getHeadersList")
    void test(HttpHeaders headers, boolean equals) {
        // 目标Controller的URL
        String url = "http://localhost:%s/processTextPlain".formatted(port);
        // 要发送的text/plain格式的数据
        String requestBody = "Hello, this is a text/plain message.<h1>语言</h1>";
        // 创建RestTemplate
        RestTemplate restTemplate = new RestTemplate();
        // 设置请求体
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        // 发送请求并获取响应
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // 处理响应
        String responseBody = responseEntity.getBody();
        if (equals) {
            Assertions.assertThat(responseBody).isEqualTo("Message processed successfully, origin request is %s".formatted(requestBody));
        } else {
            Assertions.assertThat(responseBody).isNotEqualTo("Message processed successfully, origin request is %s".formatted(requestBody));

        }
    }

}
