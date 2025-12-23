package com.whalefall.learncases.tracer.mainmethod;

import io.opentracing.Scope;
import io.opentracing.ScopeManager;
import io.opentracing.Span;
import org.slf4j.MDC;

/**
 * 适配 OpenTracing 0.33.0 接口的自定义 MDC 管理器
 */
public class CustomMDCScopeManager implements ScopeManager {

    // 存放当前的 Scope 包装器
    private final ThreadLocal<CustomMDCScope> tlsScope = new ThreadLocal<>();

    @Override
    public Scope activate(Span span) {
        return new CustomMDCScope(span);
    }

    /**
     * 【修复点 1】必须实现 activeSpan() 而不是 active()
     * 接口要求返回当前激活的 Span，如果当前没有 Scope，则返回 null
     */
    @Override
    public Span activeSpan() {
        CustomMDCScope scope = tlsScope.get();
        return scope == null ? null : scope.wrapped;
    }

    public class CustomMDCScope implements Scope {
        private final CustomMDCScope previous;
        // 让外部类可以访问 wrapped，因为 Scope 接口本身不提供 span() 方法
        final Span wrapped;
        private final String previousTraceId;
        private final String previousSpanId;
        private final String previousParentId;

        CustomMDCScope(Span span) {
            this.wrapped = span;
            this.previous = CustomMDCScopeManager.this.tlsScope.get();
            CustomMDCScopeManager.this.tlsScope.set(this);

            // 1. 保存旧值 (Snapshot)
            this.previousTraceId = MDC.get("traceId");
            this.previousSpanId = MDC.get("spanId");
            this.previousParentId = MDC.get("parentId");

            // 2. 注入新值
            if (span != null) {
                MDC.put("traceId", span.context().toTraceId());
                MDC.put("spanId", span.context().toSpanId());
            }
        }

        @Override
        public void close() {
            CustomMDCScopeManager.this.tlsScope.set(previous);

            // 3. 恢复旧值
            restoreMDC("traceId", previousTraceId);
            restoreMDC("spanId", previousSpanId);
            restoreMDC("parentId", previousParentId);
        }

        /**
         * 【修复点 2】Scope 接口中没有 span() 方法，所以去掉了 @Override
         * 这个方法保留着作为一个普通的 getter 也可以，或者你可以直接删掉它。
         */
        @SuppressWarnings("unused")
        public Span span() {
            return wrapped;
        }

        private void restoreMDC(String key, String value) {
            if (value != null) {
                MDC.put(key, value);
            } else {
                MDC.remove(key);
            }
        }
    }
}
