package com.whalefall.lambda;

import com.whalefall.AbstractTest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author Halcyon
 * @date 2024/6/9 12:59
 * @since 1.0.0
 */
class DelayCreateTests extends AbstractTest {

    static final String HELL_WORLD = "hell world";
    // 定义 Consumer<HttpPost> 属性
    private final Consumer<HttpPost> httpPostConsumer = httpPost -> {
        httpPost.setHeader("Content-Type", "text/plain; charset=UTF-8");
        httpPost.setHeader("Accept-Charset", StandardCharsets.UTF_8.name());
    };
    @LocalServerPort
    private int port;

    private static String extractResponse(HttpEntity httpEntity) {
        try {
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testDelayCreate() throws IOException {
        String url = String.format("http://localhost:%s/processTextPlain", port);
        HttpPost httpPost = getHttpPostFunction().apply(url, HELL_WORLD);
        httpPostConsumer.accept(httpPost);
        String s = doHttpPost(httpPost);
        Assertions.assertThat(s).isEqualTo("Message processed successfully, origin request is %s".formatted(HELL_WORLD));
    }

    String doHttpPost(HttpPost httpPost) throws IOException {

        // 创建HttpClient
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建HttpPost请求
            // 发送请求并获取响应
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 处理响应
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    return extractResponse(entity);
                }

            }
        }
        return null;
    }

    private BiFunction<String, String, HttpPost> getHttpPostFunction() {
        return (url, msg) -> {
            HttpPost httpPost = new HttpPost(url);
            // 设置请求体
            StringEntity stringEntity = new StringEntity(msg, StandardCharsets.UTF_8);
            httpPost.setEntity(stringEntity);
            // 设置头部信息
            httpPost.setHeader("Content-Type", "text/plain");
            httpPost.setHeader("Accept-Charset", StandardCharsets.UTF_8.name());
            return httpPost;
        };
    }

    // already define a field
    @SuppressWarnings("unused")
    private Consumer<HttpPost> getConsumer() {
        return httpPost -> {
            // this worked
            httpPost.setHeader("Content-Type", "text/plain; charset=UTF-8");
            httpPost.setHeader("Accept-Charset", StandardCharsets.UTF_8.name());
        };
    }

}
