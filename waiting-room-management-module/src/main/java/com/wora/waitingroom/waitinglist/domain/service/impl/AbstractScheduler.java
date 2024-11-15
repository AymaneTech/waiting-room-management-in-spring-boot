package com.wora.waitingroom.waitinglist.domain.service.impl;

import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.service.Scheduler;

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
