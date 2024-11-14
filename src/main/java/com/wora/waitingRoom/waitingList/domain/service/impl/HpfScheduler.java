package com.wora.waitingRoom.waitingList.domain.service.impl;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.exception.MultipleWaitingListsFoundException;
import com.wora.waitingRoom.waitingList.domain.service.Scheduler;

import java.util.*;

public class HpfScheduler implements Scheduler {
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
