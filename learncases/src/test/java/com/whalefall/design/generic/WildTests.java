package com.whalefall.design.generic;

import com.whalefall.learncases.override.Father;
import com.whalefall.learncases.override.Grandfather;
import com.whalefall.learncases.override.Son;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author WhaleFall
 * @date 2024-06-10 23:08
 */
@Slf4j
public class WildTests {

    /**
     * Lower Limit is not within its bound(s)
     * <pre>
     *  {@code Collection<? extends Father> c}
     * <pre/>
     * Only can use upper limit
     * {@code Collection<? extends Father>}
     */
    public static final Collection<? super Father> WILD_FATHER_COLLECTION = new ArrayList<>();
    public static final Collection<? super Grandfather> WILD_GRANDFATHER_COLLECTION = new ArrayList<>();

    /**
     * {@code Collection<? extends Son>}
     */
    public static final Collection<? super Son> WILD_SON_COLLECTION = new ArrayList<>();

    public static final Collection<?> WILD_COLLECTION = new ArrayList<>();
    public static final Collection<Object> OBJECT_COLLECTION = new ArrayList<>();

    private static void assertCollection(int realSize, int expectedSize) {
        assertEquals(expectedSize, realSize, String.format("Collection should contain %d element", 3));
    }

    @Test
    void testWildGrandFather() {
        WILD_GRANDFATHER_COLLECTION.add(new Father());
        WILD_GRANDFATHER_COLLECTION.add(new Son());
        WILD_GRANDFATHER_COLLECTION.add(new Grandfather());
        assertCollection(WILD_GRANDFATHER_COLLECTION.size(), 3);
    }

    @Test
    @SuppressWarnings("all")
    void testWildFather() {
        WILD_FATHER_COLLECTION.add(new Father());
        WILD_FATHER_COLLECTION.add(new Son());
        // ERROR COMPLATION ERROR TYPE MISMATCH
        // WILD_FATHER_COLLECTION.add(new Grandfather());
        // ERROR Grandfather cannot be Father
        // WILD_FATHER_COLLECTION.add((Father) new Grandfather());
        assertCollection(WILD_FATHER_COLLECTION.size(), 2);
    }

    @Test
    @SuppressWarnings("all")
    void testWildSon() {
        WILD_SON_COLLECTION.add(new Son());
        /*
         * 子类可以赋值给父类，但是反过来不行
         */
        // SON_COLLECTION.add(new Father());
        assertCollection(WILD_SON_COLLECTION.size(), 1);

    }

    @Test
    @SuppressWarnings("all")
    void testWild() {
        // WILD_COLLECTION.add(1);
        // WILD_COLLECTION.add("asf");;
        // WILD_COLLECTION.add(new Object());
        WILD_COLLECTION.add(null);
        assertCollection(WILD_COLLECTION.size(), 1);

    }

    @Test
    void testObjectWild() {
        OBJECT_COLLECTION.add(1);
        OBJECT_COLLECTION.add(new BigDecimal("123.23"));
        OBJECT_COLLECTION.add("asf");
        OBJECT_COLLECTION.add(new Object());
        OBJECT_COLLECTION.add(null);
        assertCollection(OBJECT_COLLECTION.size(), 5);
    }
}

