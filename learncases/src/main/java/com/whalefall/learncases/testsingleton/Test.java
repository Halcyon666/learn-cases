package com.whalefall.learncases.testsingleton;

import lombok.extern.slf4j.Slf4j;

/**
 * @author WhaleFall
 * @date 2022-07-27 22:21
 */
@Slf4j
public class Test {

    public static final String DASH = "----------------dash----------------";

    public static void main(String[] args) {
        Singleton instance1 = Singleton.getSingleton();
        instance1.put("a", "1");
        Singleton instance2 = Singleton.getSingleton();
        instance2.put("b", "2");

        // 单例对象 因为只实例化一次 所以对象内部的 非static变量 也会只有一份。
        instance1.getMap().forEach((k, v) -> log.info(k + " @ " + v));
        log.info(DASH);
        instance2.getMap().forEach((k, v) -> log.info(k + " @ " + v));
        log.info(DASH);

        Singleton newSingleton = Singleton.getNewSingleton();
        newSingleton.put("a", "1");
        Singleton newSingleton1 = Singleton.getNewSingleton();
        newSingleton1.put("c", "3");

        newSingleton.getMap().forEach((k, v) -> log.info(k + " @ " + v));
        log.info(DASH);
        newSingleton1.getMap().forEach((k, v) -> log.info(k + " @ " + v));

        /*
        output:
                a @ 1
                b @ 2
                ----------------dash----------------
                a @ 1
                b @ 2
                ----------------dash----------------
                a @ 1
                ----------------dash----------------
                c @ 3
         */

    }
}
