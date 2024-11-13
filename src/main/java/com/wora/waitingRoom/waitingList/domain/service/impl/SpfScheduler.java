package com.wora.waitingRoom.waitingList.domain.service.impl;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.service.Scheduler;

import java.util.List;

public class SpfScheduler implements Scheduler {
    @Override
    public List<Visit> schedule(List<Visit> visits) {
        return List.of();
    }
}
