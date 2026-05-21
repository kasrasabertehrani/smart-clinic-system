package com.smartclinicsystem;


import com.smartclinicsystem.domain.exception.NotSharpTimeException;
import com.smartclinicsystem.domain.vo.SharpTime;
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
    void testSharpTimeOfLegalTime(){
        SharpTime sharpTime = SharpTime.of(22);
        assertEquals(LocalTime.of(22, 0), sharpTime.time());
    }
    @Test
    void testSharpTimeOfIllegalTime(){
        assertThrows(IllegalArgumentException.class, () -> {
            SharpTime.of(24);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            SharpTime.of(-1);
        });
    }
}
