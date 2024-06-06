package com.whalefall.learncases.future;


import jakarta.annotation.Nullable;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock to notify across threads
 *
 * @since 1.0.0
 */
public class ReentrantLockFuture implements Future<String> {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean done = false;
    private String msg;

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
        lock.lock();
        try {
            return done;
        } finally {
            lock.unlock();
        }
    }

    public void putMsgAndReleaseLock(String msg) {
        lock.lock();
        try {
            this.msg = msg;
            this.done = true;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String get() throws InterruptedException, ExecutionException {
        lock.lock();
        try {
            while (!done) {
                condition.await();
            }
            return msg;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String get(long timeout, @Nullable TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Assert.notNull(unit, "TimeUnit must not be null");

        lock.lock();
        try {
            while (!done) {
                boolean success = condition.await(timeout, unit);
                if (!success) {
                    throw new TimeoutException("Timeout while waiting for result");
                }
            }
            return msg;
        } finally {
            lock.unlock();
        }
    }
}
