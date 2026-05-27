package com.smartclinicsystem.domain.vo;


import com.smartclinicsystem.domain.exception.NotSharpTimeException;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

public class SharpTimeTest {
    @Test
    void testSharpTimeOnSharpTime(){
        LocalTime time = LocalTime.of(10, 0);

        assertEquals(time, new SharpTime(time).time());
    }
    @Test
    void testSharpTimeOnNotSharpMinutes(){
        LocalTime time = LocalTime.of(10, 5);

        assertThrows(NotSharpTimeException.class, () -> {
            new SharpTime(time);
        });
    }
    @Test
    void testSharpTimeOnNotSharpSeconds(){
        LocalTime time = LocalTime.of(12, 0, 30);
        assertThrows(NotSharpTimeException.class, () -> {
            new SharpTime(time);
        });
    }
    @Test
    void testSharpTimeOnNotSharpNanoSeconds(){
        LocalTime time = LocalTime.of(12, 0, 0, 500000000);
        assertThrows(NotSharpTimeException.class, () -> {
            new SharpTime(time);
        });
    }
    @Test
    void testSharpTimeOfIllegalTime(){
        assertThrows(IllegalArgumentException.class, () -> {
            SharpTime.of(24, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SharpTime.of(-1, 0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SharpTime.of(0, 60);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SharpTime.of(0, -1);
        });
    }
    @Test
    void testSharpTimeWithLegalTime(){
        assertDoesNotThrow(() -> {
            SharpTime.of(23, 15);
        });
    }
    @Test
    void testSharpTimeIsBeforeAndIsAfter(){
        SharpTime time1 = SharpTime.of(10, 0);
        SharpTime time2 = SharpTime.of(11, 0);
        SharpTime time3 = SharpTime.of(9, 45);

        assertTrue(time1.isBefore(time2));
        assertFalse(time1.isBefore(time3));
        assertTrue(time1.isAfter(time3));
        assertFalse(time1.isAfter(time2));
    }
    @Test
    void testSharpTimePlus(){
        SharpTime sharpTime = SharpTime.of(10, 0);
        SharpTime newTime = sharpTime.plus(java.time.Duration.ofMinutes(30));
        assertEquals(LocalTime.of(10, 30), newTime.time());
    }

}
