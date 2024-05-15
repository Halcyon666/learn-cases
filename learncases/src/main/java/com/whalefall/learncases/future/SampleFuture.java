package com.whalefall.learncases.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Self implement AQS to notify across threads
 *
 * @author Halcyon
 * @since 1.0.0
 */
public class SampleFuture implements Future<String> {
    private final Sync sync;
    private String msg;

    public SampleFuture() {
        this.sync = new Sync();
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
        return sync.isDone();
    }

    public void putMsgAndReleaseLock(String msg) {
        this.msg = msg;
        sync.tryRelease(0);
    }

    @Override
    public String get() throws InterruptedException, ExecutionException {
        // 一直阻塞
        sync.tryAcquire(1);
        return this.msg;
    }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!sync.tryAcquireNanos(1, unit.toNanos(timeout))) {
            throw new TimeoutException("Timeout while waiting for result");
        }
        return this.msg;
    }

    static class Sync extends AbstractQueuedSynchronizer {
        public static final int LOCKED = 0;
        public static final int UNLOCKED = 1;

        Sync() {
            setState(LOCKED);
        }

        // Acquire the lock
        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(UNLOCKED, LOCKED);
        }

        /**
         * Release the lock
         *
         * @param arg the state of released lock
         * @return release lock successfully or not
         */
        @Override
        protected boolean tryRelease(int arg) {
            setState(UNLOCKED);
            return true;
        }

        public boolean isDone() {
            return getState() == UNLOCKED;
        }
    }
}
