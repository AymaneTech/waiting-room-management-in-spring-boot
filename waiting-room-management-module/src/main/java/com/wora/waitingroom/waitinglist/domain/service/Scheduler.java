package com.wora.waitingroom.waitinglist.domain.service;

import com.wora.waitingroom.waitinglist.domain.entity.Visit;

import java.util.List;

public interface Scheduler {
    List<Visit> schedule(List<Visit> visits);
}
