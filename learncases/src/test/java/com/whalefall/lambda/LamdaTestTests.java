package com.whalefall.lambda;


import com.whalefall.learncases.lambda.LamdaTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
class LamdaTestTests {

    @Test
    void testGetList() {
        List<Integer> list = LamdaTest.getList();
        assertEquals(1, list.size(), "List size should be 1");
        assertEquals(24, list.get(0), "List element should be 24");
    }

    @Test
    void testTestFunctionFalse() {
        Consumer<Boolean> falseConsumer = mock(Consumer.class);
        Runnable trueRunnable = mock(Runnable.class);

        LamdaTest.isTureOrFalse(false).trueOrFalseHandle(trueRunnable, falseConsumer);
        verify(falseConsumer, times(1)).accept(false);
        verify(trueRunnable, never()).run();
    }

    @Test
    void testTestFunctionTrue() {
        Consumer<Boolean> falseConsumer = mock(Consumer.class);
        Runnable trueRunnable = mock(Runnable.class);

        LamdaTest.isTureOrFalse(true).trueOrFalseHandle(trueRunnable, falseConsumer);
        verify(trueRunnable, times(1)).run();
        verify(falseConsumer, never()).accept(any());
    }

    @Test
    void testLog() {
        // This is a simple example to verify that the log method works
        Assertions.assertThatNoException().isThrownBy(() -> LamdaTest.log(null));
    }
}

