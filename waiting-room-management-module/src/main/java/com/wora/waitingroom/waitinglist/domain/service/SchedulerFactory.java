package com.wora.waitingroom.waitinglist.domain.service;

import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SchedulerFactory {

    private final Map<String, Scheduler> schedulers;

    public SchedulerFactory(
            @Qualifier("fifoScheduler") Scheduler fifoScheduler,
            @Qualifier("hpfScheduler") Scheduler hpfScheduler,
            @Qualifier("sjfScheduler") Scheduler sjfScheduler
    ) {
        this.schedulers = Map.of(
                "FIFO", fifoScheduler,
                "HPF", hpfScheduler,
                "SJF", sjfScheduler
        );
    }

    public Scheduler getScheduler(Algorithm algorithm) {
        Scheduler scheduler = schedulers.get(algorithm.name());
        if (scheduler == null) {
            throw new IllegalArgumentException("Unsupported scheduling algorithm: " + algorithm);
        }
        return scheduler;
    }
}
