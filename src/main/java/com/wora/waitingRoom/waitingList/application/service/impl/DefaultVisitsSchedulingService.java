package com.wora.waitingRoom.waitingList.application.service.impl;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.application.mapper.VisitMapper;
import com.wora.waitingRoom.waitingList.application.service.VisitsSchedulingService;
import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.repository.VisitRepository;
import com.wora.waitingRoom.waitingList.domain.repository.WaitingListRepository;
import com.wora.waitingRoom.waitingList.domain.service.Scheduler;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
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
    private final Scheduler scheduler;
    private final VisitMapper mapper;

    @Override
    public Page<VisitResponseDto> scheduleVisits(WaitingListId waitingListId) {
        return scheduleVisitsByStatus(waitingListId, Status.WAITING);
    }

    @Override
    public Page<VisitResponseDto> scheduleVisitsByStatus(WaitingListId waitingListId, Status status) {
        if (!waitingListRepository.existsById(waitingListId))
            throw new EntityNotFoundException("waiting list", waitingListId.value());

        List<Visit> visits = repository.findAllByWaitingListIdAndStatus(waitingListId, status);
        List<VisitResponseDto> scheduledVisits = scheduler.schedule(visits).stream()
                .map(mapper::toResponseDto)
                .toList();

        return new PageImpl<>(scheduledVisits);
    }
}
