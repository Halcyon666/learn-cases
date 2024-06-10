package com.whalefall.design.generic;

import com.whalefall.learncases.generic.Impl;
import com.whalefall.learncases.override.Father;
import com.whalefall.learncases.override.Son;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author WhaleFall
 * @date 2022-07-26 20:13
 */
@Slf4j
class TestGenericTests {

    /**
     * k-v
     * <pre>
     *     {@code TestGenericTests#stringObjMap.put("father", father);}
     * </pre>
     */
    private static final HashMap<String, Object> stringObjMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getObject(String k) {
        return (T) stringObjMap.get(k);
    }

    @BeforeEach
    void setUp() {
        stringObjMap.clear();
    }

    @Test
    void testGenericInterface() {
        // 使用 mock 替换 Impl 实例
        Impl i = mock(Impl.class);
        Father father = new Father();
        Son son = new Son();
        when(i.getInstance(father)).thenReturn(son);

        Son instance = i.getInstance(father);
        assertNotNull(instance);
    }


    @Test
    void testGetObject() {
        // 测试 getObject 方法
        Father father = new Father();
        stringObjMap.put("father", father);

        Father retrievedFather = getObject("father");
        assertNotNull(retrievedFather);
        assertEquals(father, retrievedFather);
    }
}

