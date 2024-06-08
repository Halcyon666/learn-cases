package com.whalefall.future;

import com.whalefall.learncases.future.SampleFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Halcyon
 * @since 1.0.0
 */
@Slf4j
class SampleFutureTests {

    static final String MSG = "Hello world";

    @Test
    void testFutureNotTimeOut0() throws ExecutionException, InterruptedException {
        final SampleFuture sampleFuture = new SampleFuture();
        // synchronously release lock
        sampleFuture.putMsgAndReleaseLock(MSG);

        String actual = sampleFuture.get();
        Assertions.assertEquals(MSG, actual);
    }

    @Test
    void testFutureNotTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        final SampleFuture sampleFuture = new SampleFuture();
        // asynchronously release lock
        new Thread(() -> sampleFuture.putMsgAndReleaseLock(MSG)).start();
        String actual = sampleFuture.get(1, TimeUnit.SECONDS);
        Assertions.assertEquals(MSG, actual);
    }

    @Test
    @SuppressWarnings("all")
    void testFutureTimeOut() {
        final SampleFuture sampleFuture = new SampleFuture();
        new Thread(() -> {
            // wait until the Future time out
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sampleFuture.putMsgAndReleaseLock(MSG);
        }).start();
        Assertions.assertThrows(TimeoutException.class, () -> sampleFuture.get(1, TimeUnit.SECONDS));
    }
}
