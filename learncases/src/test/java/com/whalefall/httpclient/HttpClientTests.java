package com.whalefall.httpclient;

import com.whalefall.AbstractTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Controller  {@code @PostMapping(value = "/processTextPlain", produces = "text/plain; charset=UTF-8")}
 *
 * <p>1. {@link this#getHttpPost1(String, String)}
 * <pre>
 *   {@code
 *   HttpPost httpPost = new HttpPost(url);
 *   httpPost.setHeader("Content-Type", "text/plain");
 *   httpPost.setHeader("Accept-Charset", StandardCharsets.UTF_8.name());
 *   }
 * </pre>
 * <p>2. {@link this#getHttpPost2(String, String)}
 * <pre>
 *   {@code
 *   HttpPost httpPost = new HttpPost(url);
 *   httpPost.setHeader("Content-Type", "text/plain; charset=UTF-8");
 *   }
 * </pre>
 * <p>3. {@link this#getHttpPost3(String, String)}
 * <pre>
 *   {@code
 *   HttpPost httpPost = new HttpPost(url);
 *   httpPost.setHeader("Content-Type", "text/plain");
 *   }
 * </pre>
 *
 * @author Halcyon
 * @date 2024/6/9 10:20
 * @since 1.0.0
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class HttpClientTests extends AbstractTest {
    // 要发送的text/plain格式的数据
    static final String REQUEST_BODY = "Hello, this is a text/plain message.<h1>语言</h1>";

    @LocalServerPort
    private int port;

    private static String extractResponse(HttpEntity httpEntity) {
        try {
            return EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpPost getHttpPost1(String url, String requestBody) {
        HttpPost httpPost = new HttpPost(url);

        // 设置请求体
        StringEntity stringEntity = new StringEntity(requestBody, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);
        // set this way
        httpPost.setHeader("Content-Type", "text/plain");
        httpPost.setHeader("Accept-Charset", StandardCharsets.UTF_8.name());
        return httpPost;
    }

    private static HttpPost getHttpPost3(String url, String requestBody) {
        HttpPost httpPost = new HttpPost(url);

        // 设置请求体
        StringEntity stringEntity = new StringEntity(requestBody, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);
        // set this way
        httpPost.setHeader("Content-Type", "text/plain");
        return httpPost;
    }

    private static HttpPost getHttpPost2(String url, String requestBody) {
        HttpPost httpPost = new HttpPost(url);

        // 设置请求体
        StringEntity stringEntity = new StringEntity(requestBody, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);
        // this worked
        httpPost.setHeader("Content-Type", "text/plain; charset=UTF-8");

        httpPost.setHeader("Accept-Charset", StandardCharsets.UTF_8.name());
        return httpPost;
    }

    @ParameterizedTest
    @MethodSource("getHttpPosts")
    void testHttpClient(HttpPost httpPost) throws IOException {

        String s = doHttpPost(httpPost);
        Assertions.assertThat(s).isEqualTo("Message processed successfully, origin request is %s".formatted(REQUEST_BODY));
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
                    return HttpClientTests.extractResponse(entity);
                }

            }
        }
        return null;
    }

    private List<HttpPost> getHttpPosts() {
        String url = String.format("http://localhost:%s/processTextPlain", port);
        return List.of(
                getHttpPost1(url, REQUEST_BODY),
                getHttpPost2(url, REQUEST_BODY),
                getHttpPost3(url, REQUEST_BODY)
        );
    }
}
