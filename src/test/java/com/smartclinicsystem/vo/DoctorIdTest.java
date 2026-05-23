package com.smartclinicsystem.vo;

import com.smartclinicsystem.domain.vo.DoctorId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoctorIdTest {
    @Test
    void testDoctorIdWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> new DoctorId(""));
    }
    @Test
    void testDoctorIdOnNullId() {
        assertThrows(IllegalArgumentException.class, () -> new DoctorId(null));
    }

    @Test
    void testDoctorIdWithValidId() {
        DoctorId doctorId = new DoctorId("DOC123");
        assertEquals("DOC123", doctorId.value());
    }

    @Test
    void testDoctorIdWithWhitespaceOnly() {
        assertThrows(IllegalArgumentException.class, () -> new DoctorId("   "));
    }

    @Test
    void testDoctorIdEquality() {
        DoctorId id1 = new DoctorId("DOC123");
        DoctorId id2 = new DoctorId("DOC123");
        assertEquals(id1, id2);
    }

    @Test
    void testDoctorIdInequality() {
        DoctorId id1 = new DoctorId("DOC123");
        DoctorId id2 = new DoctorId("DOC456");
        assertNotEquals(id1, id2);
    }

}
