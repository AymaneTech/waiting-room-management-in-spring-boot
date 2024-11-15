package com.wora.waitingRoom.waitingList.application.service.impl;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.application.mapper.VisitMapper;
import com.wora.waitingRoom.waitingList.application.service.VisitsSchedulingService;
import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.repository.VisitRepository;
import com.wora.waitingRoom.waitingList.domain.service.Scheduler;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultVisitsSchedulingService implements VisitsSchedulingService {
    private final VisitRepository repository;
    private final Scheduler scheduler;
    private final VisitMapper mapper;

    @Override
    public Page<VisitResponseDto> scheduleVisits(VisitId visitId) {
        if (!repository.existsById(visitId))
            throw new EntityNotFoundException("there is no visit for waiting list id: " + visitId.waitingListId().value() + " and visitor id: " + visitId.visitorId().value());
        List<Visit> visits = repository.findAllByWaitingListIdAndStatus(visitId, Status.WAITING);

        List<VisitResponseDto> scheduledVisits = scheduler.schedule(visits).stream()
                .map(mapper::toResponseDto)
                .toList();
        return new PageImpl<>(scheduledVisits);
    }
}
