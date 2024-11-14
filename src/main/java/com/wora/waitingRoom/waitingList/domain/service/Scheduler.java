package com.wora.waitingRoom.waitingList.domain.service;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;

import java.util.List;

public interface Scheduler {
    List<Visit> schedule(List<Visit> visits);

    default boolean areVisitsForSameWaitingList(List<Visit> visits) {
        return visits.stream()
                .map(Visit::getWaitingList)
                .map(WaitingList::getId)
                .distinct()
                .count() != 1;
    }
}
