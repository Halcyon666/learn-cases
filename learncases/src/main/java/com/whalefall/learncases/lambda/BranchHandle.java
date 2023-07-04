package com.whalefall.learncases.lambda;

import java.util.function.Consumer;

@FunctionalInterface
public interface BranchHandle {

    /**
     * 分支操作
     *
     * @param trueHandle  为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     **/
    void trueOrFalseHandle(Runnable trueHandle, Consumer<Boolean> falseHandle);
}
