package com.whalefall.learncases.lambda;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class LamdTest {
    public static List<Integer> getList() {
        return Stream.of(new String[]{"1", "2"}).map(s -> Integer.parseInt(s) * 2).collect(Collectors.toList());
    }

    public static void main(String[] args) {

        getList().forEach(System.out::println);
        testFunction(false);
        testFunction(true);
    }

    private static void testFunction(boolean b) {
        isTureOrFalse(b).trueOrFalseHandle(() -> log.info("{}", b), System.out::println);
    }

    /**
     * <a href="https://mp.weixin.qq.com/s/vFgEDY_mBJX7u97du6MMsA">代码来自</a>
     * <p>
     * 参数为true或false时，分别进行不同的操作
     *
     * @param b b
     * @return com.example.demo.func.BranchHandle
     **/
    public static BranchHandle isTureOrFalse(boolean b) {

        return (trueHandle, falseHandle) -> {
            if (b) {
                trueHandle.run();
            } else {
                falseHandle.accept(b);
            }
        };
    }
}
