package com.whalefall.learncases.mojibake;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class RestTemplateExample {

    public static void main(String[] args) {
        // 目标Controller的URL
        String url = "http://localhost:8080/processTextPlain";

        // 要发送的text/plain格式的数据
        String requestBody = "Hello, this is a 中秋text/plain message.<h1>语言</h1>";

        // 创建RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        // method1: this worked
//        headers.setContentType(new MediaType("text", "plain", StandardCharsets.UTF_8));

        // method2: this worked
//        headers.setContentType(MediaType.valueOf("text/plain;charset=UTF-8"));

        // method 3: this doesn't work
//        headers.setContentType(MediaType.TEXT_PLAIN);
//        headers.set("Accept-Charset", StandardCharsets.UTF_8.name());
        // same to above
//        headers.set("Content-Type", "application/json");
//        headers.set("Accept-Charset", "UTF-8");

        // method4: this worked
//        headers.set("Content-Type", "text/plain; charset=UTF-8");


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
        log.info(responseBody);
    }
}
