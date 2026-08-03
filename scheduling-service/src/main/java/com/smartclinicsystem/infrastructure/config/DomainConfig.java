package com.smartclinicsystem.infrastructure.config;

import com.smartclinicsystem.domain.service.AppointmentConflictDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public AppointmentConflictDomainService appointmentConflictDomainService() {
        return new AppointmentConflictDomainService();
    }
}