package com.smartclinicsystem.vo;


import com.smartclinicsystem.domain.vo.PatientId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PatientIdTest {
    @Test
    void testPatientIdWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> new PatientId(""));
    }
    @Test
    void testPatientIdOnNullId() {
        assertThrows(IllegalArgumentException.class, () -> new PatientId(null));
    }

    @Test
    void testPatientIdWithValidId() {
        PatientId PatientId = new PatientId("DOC123");
        assertEquals("DOC123", PatientId.value());
    }

    @Test
    void testPatientIdWithWhitespaceOnly() {
        assertThrows(IllegalArgumentException.class, () -> new PatientId("   "));
    }

    @Test
    void testPatientIdEquality() {
        PatientId id1 = new PatientId("DOC123");
        PatientId id2 = new PatientId("DOC123");
        assertEquals(id1, id2);
    }

    @Test
    void testPatientIdInequality() {
        PatientId id1 = new PatientId("DOC123");
        PatientId id2 = new PatientId("DOC456");
        assertNotEquals(id1, id2);
    }

}
