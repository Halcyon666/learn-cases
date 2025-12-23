package com.whalefall.learncases.tracer.mainmethod;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.JaegerSpanContext;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.math.BigInteger;

/**
 * @author Halcyon
 * @since 2025/12/23 21:45
 */
@Slf4j
public class TraceWithMDC {

    static Tracer tracer = new Configuration("test-service")
            .withSampler(new Configuration.SamplerConfiguration().withType("const").withParam(1))
            .withReporter(new Configuration.ReporterConfiguration().withLogSpans(true))
            .getTracerBuilder() // 1. 使用 Builder 模式
            .withScopeManager(new CustomMDCScopeManager()) // 2. 使用刚才新建的类
            .build();

    public void doTracer(String parentSpanHex, String traceIdHex, String parentIdHex) {
        // traceId high and low parts (64-bit each)
        long[] traceIdParts = splitTraceId(traceIdHex);
        long traceIdHigh = traceIdParts[0];
        long traceIdLow = traceIdParts[1];

        long parentSpanId = Long.parseUnsignedLong(parentSpanHex, 16);

        /*
        | Parameter     | Meaning                                                                      |
        | ------------- | ---------------------------------------------------------------------------- |
        | `traceIdHigh` | High 64 bits of the 128-bit traceId                                          |
        | `traceIdLow`  | Low 64 bits of the traceId                                                   |
        | `spanId`      | The **spanId of this span** (in your case, the remote parent span)           |
        | `parentId`    | The **spanId of this span’s parent** (i.e., the parent of the remote parent) |
        | `flags`       | Sampling flags (usually `(byte)1` if sampled)                                |
         */
        JaegerSpanContext parentContext = new JaegerSpanContext(
                traceIdHigh,
                traceIdLow,
                parentSpanId,
                Long.parseUnsignedLong(parentIdHex, 16),               // parent of parent unknown
                (byte) 1        // sampled
        );

        Span childSpan = tracer.buildSpan("child-span-test")
                .asChildOf(parentContext)                     // link to remote parent
                .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_SERVER)  // optional
                .start();
        // 字符串
        childSpan.setTag("db.statement", "SELECT * FROM users");
        // 数字
        childSpan.setTag("http.status_code", 200);
        // 布尔值
        childSpan.setTag("error", false); // 标记是否出错
        // 自定义业务字段
        childSpan.setTag("order.id", "ORD-20251223-001");

        // 这个值会被下游服务读取到，哪怕下游服务离你隔着好几层
        childSpan.setBaggageItem("transaction.user_level", "VIP");
        // 5. 【关键】激活 Span 到当前线程
        try (Scope ignore = tracer.scopeManager().activate(childSpan)) {
            MDC.put("traceId", childSpan.context().toTraceId());
            MDC.put("spanId", childSpan.context().toSpanId());
            // --- 业务执行区 ---
            doBusinessLogic();
            Thread.sleep(100);
            // ----------------
        } catch (Exception e) {
            // 记录异常到 Span (标准操作)
            Tags.ERROR.set(childSpan, true);
            childSpan.log(java.util.Map.of("event", "error", "message", e.getMessage()));
            throw new RuntimeException(e);
        } finally {
            // === 核心修改 START ===
            // 必须清理，防止线程池复用导致 TraceId 污染
            MDC.remove("traceId");
            MDC.remove("spanId");
            childSpan.finish();
        }
    }

    private void doBusinessLogic() {
        log.info("Executing business logic...");
    }

    public static long[] splitTraceId(String traceIdHex) {
        if (traceIdHex.length() <= 16) {
            // Only low 64 bits, high = 0
            long low = new BigInteger(traceIdHex, 16).longValue();
            return new long[]{0L, low};
        } else {
            // Split into high and low 64 bits
            String highHex = traceIdHex.substring(0, traceIdHex.length() - 16);
            String lowHex = traceIdHex.substring(traceIdHex.length() - 16);
            long high = new BigInteger(highHex, 16).longValue();
            long low = new BigInteger(lowHex, 16).longValue();
            return new long[]{high, low};
        }
    }

    /**
     * Span reported: 04bf92f3577b34da63ce929d0e0e4736:36bd32b7a5712a1a:00f067aa0ba902b7:1 - child-span-test
     * | Field         | Value                            | Meaning                                                                 |
     * | ------------- | -------------------------------- | ----------------------------------------------------------------------- |
     * | traceId       | 04bf92f3577b34da63ce929d0e0e4736 | The **traceId** of the whole trace (128-bit). ✅ matches what you passed |
     * | spanId        | 36bd32b7a5712a1a                 | **Generated child spanId** by Jaeger. ✅ new unique span                 |
     * | parentSpanId  | 00f067aa0ba902b7                 | The **parent spanId** you provided. ✅ links to upstream span            |
     * | flags         | 1                                | Sampling flag (sampled). ✅                                              |
     * | operationName | child-span-test                  | Your span name. ✅                                                       |
     */
    public static void main(String[] args) {
        TraceWithMDC testTracer = new TraceWithMDC();
        String traceIdHex = "4bf92f3577b34da63ce929d0e0e4736";
        String parentSpanHex = "00f067aa0ba902b7";       // remote parent spanId
        String parentOfParentHex = "0011223344556677";   // remote parent's parent spanId

        // 使用 ScopeManager 激活 Span
        testTracer.doTracer(parentSpanHex, traceIdHex, parentOfParentHex);
    }

}
