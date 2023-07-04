package com.whalefall.learncases.override;

/**
 * @author WhaleFall
 * @date 2022-06-29 21:58
 * 静态的方法可以被重载 重写
 * final的方法不可重写 可以重载
 */
public class Test {
    public static void main(String[] args) {
        Son.work1();
        Father.work1();

        new Son().print("s");
        new Father().print("s");
    }
}
