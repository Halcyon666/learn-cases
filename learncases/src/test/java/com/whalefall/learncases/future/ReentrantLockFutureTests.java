package com.whalefall.learncases.future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Halcyon
 * @since 1.0.0
 */
class ReentrantLockFutureTests {

    static final String MSG = "hello world from SampleFutureTests";

    @Test
    void testFutureNotTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        final ReentrantLockFuture future = new ReentrantLockFuture();
        new Thread(() -> {
            try {
                Thread.sleep(500); // Simulate some delay
                future.putMsgAndReleaseLock(MSG);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        Assertions.assertEquals(MSG, future.get(1, TimeUnit.SECONDS));
    }

    @Test
    void testFutureTimeOut() {
        final ReentrantLockFuture future = new ReentrantLockFuture();
        Assertions.assertThrows(TimeoutException.class, () -> future.get(1, TimeUnit.SECONDS));
    }
}

