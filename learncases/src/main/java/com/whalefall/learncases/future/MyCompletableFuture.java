package com.whalefall.learncases.future;

import jakarta.annotation.Nullable;

import java.util.concurrent.*;

/**
 * Remember that this is the way most used.
 *
 * @author Halcyon
 * @since 1.0.0
 */
public class MyCompletableFuture implements Future<String> {
    private final CompletableFuture<String> future = new CompletableFuture<>();

    public void putMsgAndReleaseLock(String msg) {
        future.complete(msg);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    @Override
    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public String get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    @Override
    public String get(long timeout, @Nullable TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }
}
