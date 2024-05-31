package com.whalefall.mapstruct;

import com.whalefall.learncases.mapstruct.case4.MapStructEntity3;
import com.whalefall.learncases.mapstruct.case4.MapStructEntity4;
import com.whalefall.learncases.mapstruct.case4.Status;
import com.whalefall.learncases.mapstruct.case5.MyConverter5;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MyConverter5Tests {

    private MyConverter5 converter;

    @BeforeEach
    public void setUp() {
        converter = Mappers.getMapper(MyConverter5.class);
    }

    @Test
    void testConvert_withEmptyStatus() {
        MapStructEntity3 entity3 = new MapStructEntity3();
        entity3.setName("testName");

        MapStructEntity4 entity4 = converter.convert(entity3, "testHaha");

        assertNotNull(entity4);
        assertEquals(Status.A, entity4.getStatus());
        assertEquals("testName", entity4.getName());
    }

    @Test
    void testConvert_withSpecificStatus() {
        MapStructEntity3 entity3 = new MapStructEntity3();
        entity3.setStatus("closed");
        entity3.setName("testName");

        MapStructEntity4 entity4 = converter.convert(entity3, "testHaha");

        assertNotNull(entity4);
        assertEquals(Status.C, entity4.getStatus());
        assertEquals("testName", entity4.getName());
    }

    @Test
    void testConvert_withEmptyName() {
        MapStructEntity3 entity3 = new MapStructEntity3();
        entity3.setStatus("active");

        MapStructEntity4 entity4 = converter.convert(entity3, "testHaha");

        assertNotNull(entity4);
        assertEquals(Status.A, entity4.getStatus());
        assertEquals("whalefall", entity4.getName());
    }


    @Test
    void testConvert_withNullInput() {
        MapStructEntity3 entity3 = null;
        String haha = "haha";

        MapStructEntity4 result = converter.convert(entity3, haha);

        assertNotNull(result);
        assertEquals(Status.A, result.getStatus());
        assertEquals("whalefall", result.getName());
    }
}
