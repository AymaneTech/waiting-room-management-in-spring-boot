package com.wora.waitingroom.waitinglist.application.service.impl;

import com.wora.waitingroom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingroom.waitinglist.application.dto.response.VisitResponseDto;
import com.wora.waitingroom.waitinglist.application.mapper.VisitMapper;
import com.wora.waitingroom.waitinglist.application.service.VisitsSchedulingService;
import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.repository.VisitRepository;
import com.wora.waitingroom.waitinglist.domain.repository.WaitingListRepository;
import com.wora.waitingroom.waitinglist.domain.service.Scheduler;
import com.wora.waitingroom.waitinglist.domain.service.SchedulerFactory;
import com.wora.waitingroom.waitinglist.domain.vo.Status;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultVisitsSchedulingService implements VisitsSchedulingService {
    private final VisitRepository repository;
    private final WaitingListRepository waitingListRepository;
    private final SchedulerFactory schedulerFactory;
    private final VisitMapper mapper;

    @Override
    public Page<VisitResponseDto> scheduleVisits(WaitingListId waitingListId) {
        return scheduleVisitsByStatus(waitingListId, Status.WAITING);
    }

    @Override
    public Page<VisitResponseDto> scheduleVisitsByStatus(WaitingListId waitingListId, Status status) {
        WaitingList waitingList = waitingListRepository.findById(waitingListId)
                .orElseThrow(() -> new EntityNotFoundException("waiting list", waitingListId.value()));

        Scheduler scheduler = schedulerFactory.getScheduler(waitingList.getAlgorithm());

        List<Visit> visits = repository.findAllByWaitingListIdAndStatus(waitingListId, status);
        List<VisitResponseDto> scheduledVisits = scheduler.schedule(visits).stream()
                .map(mapper::toResponseDto)
                .toList();

        return new PageImpl<>(scheduledVisits);
    }
}
