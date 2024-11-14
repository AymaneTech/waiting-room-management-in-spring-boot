package com.wora.waitingRoom.waitingList.domain.service.impl;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.service.Scheduler;

import java.util.List;

public abstract class AbstractScheduler implements Scheduler {

    protected boolean areVisitsForSameWaitingList(List<Visit> visits) {
        return visits.stream()
                .map(Visit::getWaitingList)
                .map(WaitingList::getId)
                .distinct()
                .count() != 1;
    }

}
