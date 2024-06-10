package com.whalefall.singleton;

import com.whalefall.learncases.testsingleton.Singleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Halcyon
 * @date 2024/6/10 23:32
 * @since 1.0.0
 */
class SingletonTests {

    private Singleton instance1;
    private Singleton instance2;
    private Singleton newSingleton;
    private Singleton newSingleton1;

    @BeforeEach
    void setUp() {
        instance1 = Singleton.getSingleton();
        instance1.put("a", "1");
        instance2 = Singleton.getSingleton();
        instance2.put("b", "2");

        newSingleton = Singleton.getNewSingleton();
        newSingleton.put("a", "1");
        newSingleton1 = Singleton.getNewSingleton();
        newSingleton1.put("c", "3");
    }

    @Test
    void testInstance1Map() {
        assertEquals(2, instance1.getMap().size());
    }

    @Test
    void testInstance2Map() {
        assertEquals(2, instance2.getMap().size());
    }

    @Test
    void testNewSingletonMap() {
        assertEquals(1, newSingleton.getMap().size());
    }

    @Test
    void testNewSingleton1Map() {
        assertEquals(1, newSingleton1.getMap().size());
    }
}
