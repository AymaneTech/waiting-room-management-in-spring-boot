package com.wora.waitingRoom.waitingList.domain.service.impl;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.exception.MultipleWaitingListsFoundException;
import com.wora.waitingRoom.waitingList.domain.service.Scheduler;

import java.util.Comparator;
import java.util.List;

public class FifoScheduler implements Scheduler {
    @Override
    public List<Visit> schedule(List<Visit> visits) {
        if (!areVisitsForSameWaitingList(visits))
            throw new MultipleWaitingListsFoundException("Visits are not for the same waiting list");

        return visits.stream()
                .sorted(Comparator.comparing(Visit::getArrivalTime))
                .toList();
    }

    private boolean areVisitsForSameWaitingList(List<Visit> visits) {
        return visits.stream()
                .map(Visit::getWaitingList)
                .map(WaitingList::getId)
                .distinct()
                .count() == 1;
    }
}
