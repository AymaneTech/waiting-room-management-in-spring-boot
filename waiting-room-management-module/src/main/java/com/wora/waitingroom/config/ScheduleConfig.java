package com.wora.waitingroom.config;

import com.wora.waitingroom.config.configurationProperties.WaitingListConfigurationProperties;
import com.wora.waitingroom.waitinglist.domain.service.Scheduler;
import com.wora.waitingroom.waitinglist.domain.service.impl.FifoScheduler;
import com.wora.waitingroom.waitinglist.domain.service.impl.HpfScheduler;
import com.wora.waitingroom.waitinglist.domain.service.impl.SpfScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ScheduleConfig {
    private final WaitingListConfigurationProperties props;

    @Bean("fifoScheduler")
    public Scheduler fifoScheduler() {
        return new FifoScheduler();
    }

    @Bean("hpfScheduler")
    public Scheduler hpfScheduler() {
        return new HpfScheduler();
    }

    @Bean("sjfScheduler")
    public Scheduler sjfScheduler() {
        return new SpfScheduler();
    }
}
