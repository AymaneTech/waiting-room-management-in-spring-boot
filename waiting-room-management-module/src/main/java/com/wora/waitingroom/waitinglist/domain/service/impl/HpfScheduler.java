package com.wora.waitingroom.waitinglist.domain.service.impl;

import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.exception.MultipleWaitingListsFoundException;
import com.wora.waitingroom.waitinglist.domain.service.Scheduler;

import java.util.Comparator;
import java.util.List;

public class HpfScheduler extends AbstractScheduler implements Scheduler {
    @Override
    public List<Visit> schedule(List<Visit> visits) {
        if (visits.isEmpty())
            return List.of();

        if (areVisitsForSameWaitingList(visits))
            throw new MultipleWaitingListsFoundException("Visits are not for the same waiting list");

        return visits.stream()
                .sorted(
                        Comparator.comparing(Visit::getPriority)
                                .thenComparing(Visit::getArrivalTime)
                )
                .toList();
    }
}
