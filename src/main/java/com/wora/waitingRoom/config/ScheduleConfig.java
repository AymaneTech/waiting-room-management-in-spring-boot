package com.wora.waitingRoom.config;

import com.wora.waitingRoom.config.configurationProperties.WaitingListConfigurationProperties;
import com.wora.waitingRoom.waitingList.domain.service.Scheduler;
import com.wora.waitingRoom.waitingList.domain.service.impl.FifoScheduler;
import com.wora.waitingRoom.waitingList.domain.service.impl.HpfScheduler;
import com.wora.waitingRoom.waitingList.domain.service.impl.SpfScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleConfig {

    @Bean
    public Scheduler scheduler(WaitingListConfigurationProperties props) {
        return switch (props.algorithm()) {
            case FIFO -> new FifoScheduler();
            case HPF -> new HpfScheduler();
            case SJF -> new SpfScheduler();
            default -> throw new IllegalArgumentException("error: creating schedule implementation");
        };
    }
}
