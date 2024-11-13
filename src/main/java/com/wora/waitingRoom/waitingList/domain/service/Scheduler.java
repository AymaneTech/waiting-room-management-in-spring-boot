package com.wora.waitingRoom.waitingList.domain.service;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;

import java.util.List;

public interface Scheduler {
    List<Visit> schedule(List<Visit> visits);
}
