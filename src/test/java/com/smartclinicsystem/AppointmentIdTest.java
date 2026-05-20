package com.smartclinicsystem;

import com.smartclinicsystem.domain.vo.AppointmentId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentIdTest {

    @Test
    void testAppointmentIdWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> new AppointmentId(""));
    }
    @Test
    void testAppointmentIdOnNullId() {
        assertThrows(IllegalArgumentException.class, () -> new AppointmentId(null));
    }
    @Test
    void testAppointmentIdWithValidId() {
        AppointmentId id = new AppointmentId("APT-123");
        assertEquals("APT-123", id.value());
    }
}
