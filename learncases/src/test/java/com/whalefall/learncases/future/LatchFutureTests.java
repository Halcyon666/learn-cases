package com.whalefall.learncases.future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @since 1.0.0
 */
class LatchFutureTests {

    static final String MSG = "hello world from LatchFutureTests";

    @Test
    void testFutureNotTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        final LatchFuture latchFuture = new LatchFuture();
        new Thread(() -> {
            try {
                Thread.sleep(500); // Simulate some delay
                latchFuture.putMsgAndReleaseLock(MSG);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        Assertions.assertEquals(MSG, latchFuture.get(1, TimeUnit.SECONDS));
    }

    @Test
    void testFutureTimeOut() {
        final LatchFuture latchFuture = new LatchFuture();
        Assertions.assertThrows(TimeoutException.class, () -> latchFuture.get(1, TimeUnit.SECONDS));
    }
}

