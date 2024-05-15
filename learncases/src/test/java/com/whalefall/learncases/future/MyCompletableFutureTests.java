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
class MyCompletableFutureTests {

    static final String MSG = "hello world from SampleFutureTests";

    @Test
    void testFutureNotTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        final MyCompletableFuture completableFuture = new MyCompletableFuture();
        new Thread(() -> {
            try {
                Thread.sleep(500); // Simulate some delay
                completableFuture.putMsgAndReleaseLock(MSG);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        Assertions.assertEquals(MSG, completableFuture.get(1, TimeUnit.SECONDS));
    }

    @Test
    void testFutureTimeOut() {
        final MyCompletableFuture completableFuture = new MyCompletableFuture();
        Assertions.assertThrows(TimeoutException.class, () -> completableFuture.get(1, TimeUnit.SECONDS));
    }
}
