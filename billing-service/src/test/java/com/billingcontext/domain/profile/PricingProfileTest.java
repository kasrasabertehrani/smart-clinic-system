package com.billingcontext.domain.profile;

import com.billingcontext.domain.TestFixtures;
import com.billingcontext.domain.shared.id.DoctorId;
import com.billingcontext.domain.shared.finance.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class PricingProfileTest {

    @Test
    void testCreatePricingProfile() {
        DoctorId doctorId = new DoctorId("doctor-123");
        Money HourlyRate = TestFixtures.moneyOf("100.0", "USD");

        PricingProfile newProfile = PricingProfile.openUpPricingProfile(doctorId, HourlyRate);

        assertNotNull(newProfile.getPricingProfileId());
        assertEquals(doctorId, newProfile.getDoctorId());
        assertEquals(HourlyRate, newProfile.getHourlyRate());
        assertEquals(LocalDateTime.now().toLocalDate(),newProfile.getCreatedAt().toLocalDate());
    }
    @Test
    void testCreatePricingProfileWithZeroHourlyRate() {
        DoctorId doctorId = new DoctorId("doctor-123");
        Money HourlyRate = TestFixtures.moneyOf("0.0", "USD");

        assertThrows(Exception.class, () -> {
            PricingProfile.openUpPricingProfile(doctorId, HourlyRate);
        });
    }
    @Test
    void testChangeHourlyRate() {
        DoctorId doctorId = new DoctorId("doctor-123");
        Money HourlyRate = TestFixtures.moneyOf("100.0", "USD");

        PricingProfile newProfile = PricingProfile.openUpPricingProfile(doctorId, HourlyRate);

        newProfile.changeHourlyRate(TestFixtures.moneyOf("150.0", "USD"));
        assertEquals(TestFixtures.moneyOf("150.0", "USD"), newProfile.getHourlyRate());
        assertEquals(LocalDateTime.now().toLocalDate(),newProfile.getUpdatedAt().toLocalDate());
    }
    @Test
    void testChangeHourlyRateWithZeroHourlyRate() {
        DoctorId doctorId = new DoctorId("doctor-123");
        Money HourlyRate = TestFixtures.moneyOf("150.0", "USD");
        PricingProfile newProfile = PricingProfile.openUpPricingProfile(doctorId, HourlyRate);

        assertThrows(PricingProfileException.class, () -> {
            newProfile.changeHourlyRate(TestFixtures.moneyOf("0.0", "USD"));
        });
    }
    @Test
    void testCalculateAppointmentFee(){
        DoctorId doctorId = new DoctorId("doctor-123");
        Money HourlyRate = TestFixtures.moneyOf("100.0", "USD");

        PricingProfile newProfile = PricingProfile.openUpPricingProfile(doctorId, HourlyRate);

        Money calculateAppointmentFee = newProfile.calculateAppointmentFee(TestFixtures.bigDecimalOf("2.0"));
        assertEquals(TestFixtures.moneyUSD("200.0"), calculateAppointmentFee);
    }
    @Test
    void testCalculateAppointmentFeeWithInvalidAppointmentHours(){
        DoctorId doctorId = new DoctorId("doctor-123");
        Money HourlyRate = TestFixtures.moneyOf("100.0", "USD");

        PricingProfile newProfile = PricingProfile.openUpPricingProfile(doctorId, HourlyRate);
        assertThrows(PricingProfileException.class, () -> {
            newProfile.calculateAppointmentFee(TestFixtures.bigDecimalOf("0.0"));
        });
    }

}
