package com.whalefall.learncases.future;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.concurrent.*;

/**
 * Simple implementation using CountDownLatch for synchronization
 *
 * @since 1.0.0
 */
@Slf4j
public class LatchFuture implements Future<String> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile String msg;

    public void putMsgAndReleaseLock(String msg) {
        log.info("lock countDown");
        this.msg = msg;
        latch.countDown();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return latch.getCount() == 0;
    }

    @Override
    public String get() throws InterruptedException, ExecutionException {
        latch.await();
        return this.msg;
    }

    @Override
    public String get(long timeout, @Nullable TimeUnit unit) throws InterruptedException, TimeoutException {
        Assert.notNull(unit, "TimeUnit must not be null");
        // not get lock, timeout elapses
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException("Timeout while waiting for result");
        }
        return this.msg;
    }
}
