package com.whalefall.learncases.tracer.spring;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.whalefall.learncases.tracer.mainmethod.CustomMDCScopeManager;
import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;

@Slf4j
@Component
@Order(-2) // 确保尽早执行，以便包装 Request
public class RemoteTraceFilter implements Filter {

    // 保持 Tracer 配置不变
    private static final Tracer tracer = new Configuration("spring-service")
            .withSampler(new Configuration.SamplerConfiguration().withType("const").withParam(1))
            .withReporter(new Configuration.ReporterConfiguration().withLogSpans(true))
            .getTracerBuilder()
            .withScopeManager(new CustomMDCScopeManager())
            .build();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /*
    POST http://localhost:8080/processTextPlain
    Content-Type: application/json

    {
      "traceId": "04bf92f3577b34da63ce929d0e0e4736",
      "parentSpanId": "00f067aa0ba902b7",
      "parentOfParentId": "0011223344556677",
      "payload": {
        "userId": 123,
        "action": "purchase"
      }
    }

    23:25:48.268 [http-nio-8080-exec-3] [04bf92f3577b34da63ce929d0e0e4736,933f0a3d8f873f33]
    INFO  c.w.l.mojibake.MojibakeController - requestBody: {
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;

        // 1. 【关键】包装 Request，使其支持重复读取
        // 注意：只对包含 Body 的请求进行包装，避免不必要的开销
        CachedBodyHttpServletRequest wrappedRequest = null;
        if (shouldWrap(httpReq)) {
            try {
                wrappedRequest = new CachedBodyHttpServletRequest(httpReq);
            } catch (Exception e) {
                log.warn("Failed to cache request body", e);
            }
        }

        // 如果包装失败，降级使用原始请求（此时可能无法读取 Body）
        HttpServletRequest requestToUse = (wrappedRequest != null) ? wrappedRequest : httpReq;

        String traceIdHex = null;
        String parentSpanHex = null;
        String parentOfParentHex = null;

        // 2. 从 Wrapper 中读取 JSON，不影响后续 Controller
        try {
            if (wrappedRequest != null) {
                // readTree 会消耗当前的 Stream，但因为是 Wrapper，下次 getInputStream 会返回新的 Stream
                JsonNode body = objectMapper.readTree(wrappedRequest.getInputStream());

                if (body != null) { // 增加判空
                    if (body.has("traceId")) traceIdHex = body.get("traceId").asText();
                    if (body.has("parentSpanId")) parentSpanHex = body.get("parentSpanId").asText();
                    if (body.has("parentOfParentId")) parentOfParentHex = body.get("parentOfParentId").asText();
                }
            }
        } catch (Exception e) {
            // 这里可能会捕获 json 解析异常，但这不影响请求继续
            log.debug("Trace info not found in body or body parse error: {}", e.getMessage());
        }

        // 3. 如果没拿到 Trace 信息，直接放行（注意要放行 wrappedRequest）
        if (traceIdHex == null || parentSpanHex == null) {
            chain.doFilter(requestToUse, response);
            return;
        }

        try {
            // ... 保持原有的 ID 解析逻辑不变 ...
            long[] traceIdParts = splitTraceId(traceIdHex);
            long parentSpanId = Long.parseUnsignedLong(parentSpanHex, 16);
            long parentId = (parentOfParentHex != null && !parentOfParentHex.isEmpty())
                    ? Long.parseUnsignedLong(parentOfParentHex, 16) : 0L;

            JaegerSpanContext parentContext = new JaegerSpanContext(
                    traceIdParts[0], traceIdParts[1], parentSpanId, parentId, (byte) 1
            );

            // 4. 开启 Span 并注入 MDC
            Span childSpan = tracer.buildSpan(httpReq.getMethod() + " " + httpReq.getRequestURI())
                    .asChildOf(parentContext)
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)
                    .start();

            try (Scope scope = tracer.scopeManager().activate(childSpan)) {
                // ScopeManager 会自动处理 MDC.put，但为了保险（或你的 ScopeManager 没写全），保留手动 put 也可以
                // 但如果 CustomMDCScopeManager 写得对，下面两行其实是多余的，Scope 激活时已经 put 了
                // MDC.put("traceId", ...);

                // 【关键】传递 wrappedRequest 给下游
                chain.doFilter(requestToUse, response);
            } finally {
                childSpan.finish();
            }
        } catch (Exception e) {
            log.error("Trace filter error", e);
            chain.doFilter(requestToUse, response);
        }
    }

    // 辅助判断：只有 POST/PUT 且 Content-Type 是 JSON 时才尝试读取 Body
    private boolean shouldWrap(HttpServletRequest req) {
        String method = req.getMethod();
        String contentType = req.getContentType();
        return ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method))
               && contentType != null
               && contentType.toLowerCase().contains("application/json");
    }

    private static long[] splitTraceId(String traceIdHex) {
        // ... 保持原有拆分逻辑 ...
        if (traceIdHex.length() <= 16) {
            long low = new BigInteger(traceIdHex, 16).longValue();
            return new long[]{0L, low};
        } else {
            String highHex = traceIdHex.substring(0, traceIdHex.length() - 16);
            String lowHex = traceIdHex.substring(traceIdHex.length() - 16);
            long high = new BigInteger(highHex, 16).longValue();
            long low = new BigInteger(lowHex, 16).longValue();
            return new long[]{high, low};
        }
    }
}
