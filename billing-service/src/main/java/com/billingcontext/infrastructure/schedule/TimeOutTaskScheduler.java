package com.billingcontext.infrastructure.schedule;

import com.billingcontext.application.usecase.timeout.TimeOutUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TimeOutTaskScheduler {
    private final TimeOutUseCase timeOutUseCase;

    @Scheduled(fixedRate = 5000)
    public void tick(){
        timeOutUseCase.handelPaymentTimeOut();
    }
}
