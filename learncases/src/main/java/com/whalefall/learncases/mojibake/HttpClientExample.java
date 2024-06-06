package com.whalefall.learncases.mojibake;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
public class HttpClientExample {

    public static void main(String[] args) throws IOException {
        // 目标Controller的URL
        String url = "http://localhost:8080/processTextPlain";

        // 要发送的text/plain格式的数据
        String requestBody = "Hello, this is a text/plain message.<h1>语言</h1>";

        // 创建HttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建HttpPost请求
            HttpPost httpPost = getHttpPost(url, requestBody);
            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 处理响应
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            log.info(line);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("all")
    private static HttpPost getHttpPost(String url, String requestBody) {
        HttpPost httpPost = new HttpPost(url);

        // 设置请求体
        StringEntity stringEntity = new StringEntity(requestBody, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);
        // this worked
        // httpPost.setHeader("Content-Type", "text/plain; charset=UTF-8");
        // both this and above is work
        httpPost.setHeader("Content-Type", "text/plain");
        httpPost.setHeader("Accept-Charset", StandardCharsets.UTF_8.name());
        return httpPost;
    }
}
