package com.whalefall.learncases.future;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * code from <a href="https://medium.com/@vikas.taank_40391/java-advanced-concurrency-interview-questions-69449655ba9b">...</a>
 *
 * @author Vikas Taank
 * @date 2024/1/7 23:09
 * @since 1.0.0
 */
@Slf4j
public class ParallelProcessingAcceptAsync {

    public static void main(String[] args) {
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
                }, executor);

        allFutures.join(); // Wait for all futures including the result processing to complete
        executor.shutdown(); // Shut down the executor
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        log.info("Execution Time: {} ms", duration);
    }
}
