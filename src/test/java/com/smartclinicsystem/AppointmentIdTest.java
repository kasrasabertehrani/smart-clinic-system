package com.smartclinicsystem;

import com.smartclinicsystem.domain.vo.AppointmentId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppointmentIdTest {

    @Test
    void testAppointmentIdWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> new AppointmentId(""));
    }
    @Test
    void testAppointmentIdOnNullId() {
        assertThrows(IllegalArgumentException.class, () -> new AppointmentId(null));
    }
}
