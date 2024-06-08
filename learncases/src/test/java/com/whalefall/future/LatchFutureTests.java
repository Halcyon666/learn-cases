package com.whalefall.future;

import com.whalefall.learncases.future.LatchFuture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @since 1.0.0
 */
class LatchFutureTests {

    static final String MSG = "hello world from LatchFutureTests";

    @Test
    void testFutureNotTimeOut() throws InterruptedException, TimeoutException {
        final LatchFuture latchFuture = new LatchFuture();
        latchFuture.putMsgAndReleaseLock(MSG);
        Assertions.assertEquals(MSG, latchFuture.get(1, TimeUnit.SECONDS));
    }

    @Test
    @SuppressWarnings("all")
    void testFutureTimeOut() {
        final LatchFuture latchFuture = new LatchFuture();
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Simulate some delay
                latchFuture.putMsgAndReleaseLock(MSG);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        Assertions.assertThrows(TimeoutException.class, () -> latchFuture.get(1, TimeUnit.SECONDS));
    }
}

