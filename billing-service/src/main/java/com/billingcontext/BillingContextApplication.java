package com.billingcontext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BillingContextApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingContextApplication.class, args);
    }

}
