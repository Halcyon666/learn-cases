package com.whalefall.future;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ParallelProcessingAcceptAsyncTests {

    @Test
    void testAcceptAsync() {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        List<CompletableFuture<Integer>> futures = data.stream()
                .map(num -> CompletableFuture.supplyAsync(() -> {
                    log.info("Computation Thread: {}, processing number: {}", Thread.currentThread().getName(), num);
                    return num * num;
                }, executor))
                .toList();

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAcceptAsync(v -> {
                    log.info("Result processing Thread: {}", Thread.currentThread().getName());
                    int sum = futures.stream()
                            .map(CompletableFuture::join)
                            .mapToInt(Integer::intValue).sum();
                    log.info("Sum of squared numbers: {}", sum);
                    assertEquals(55, sum, "Sum of squared numbers should be 55");
                }, executor);

        allFutures.join(); // Wait for all futures including the result processing to complete
        executor.shutdown(); // Shut down the executor
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        log.info("Execution Time: {} ms", duration);
    }

    @Test
    void testCompletableFutureWithException() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Oops!");
        });

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2)
                .thenAccept(v -> log.info("All futures completed normally"))
                .exceptionally(ex -> {
                    log.info("Exception occurred: {}", ex.getCause().getMessage());
                    assertEquals("Oops!", ex.getCause().getMessage(), "Exception message should be 'Oops!'");
                    return null;
                });

        allFutures.join();
    }

    @Test
    void testCompletableFutureHasNull() {
        CompletableFuture<Void> allFutures = CompletableFuture.allOf();
        allFutures.thenAcceptAsync(v -> {
            log.info("Result: {}", v);
            assertNull(v, "Result should be null");
        }).join();
    }
}

