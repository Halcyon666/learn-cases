package com.whalefall.learncases.testsingleton;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WhaleFall
 * @date 2022-07-27 22:15
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class Singleton {

    private final HashMap<String, String> map = new HashMap();

    public static Singleton getSingleton() {
        return Holder.INSTANCE;
    }

    public static Singleton getNewSingleton() {
        return new Singleton();
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void put(String k, String v) {
        map.put(k, v);
    }

    private static class Holder {
        public static final Singleton INSTANCE = new Singleton();
    }
}
