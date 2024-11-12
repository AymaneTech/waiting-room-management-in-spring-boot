package com.wora.waitingRoom.waitingList.application.service.impl;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.visitor.application.service.VisitorService;
import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.visitor.domain.VisitorId;
import com.wora.waitingRoom.waitingList.application.dto.request.VisitRequestDto;
import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.application.mapper.VisitMapper;
import com.wora.waitingRoom.waitingList.application.service.VisitService;
import com.wora.waitingRoom.waitingList.application.service.WaitingListService;
import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.exception.DuplicateSubscriptionException;
import com.wora.waitingRoom.waitingList.domain.exception.WaitingListDatePassedException;
import com.wora.waitingRoom.waitingList.domain.repository.VisitRepository;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultVisitService implements VisitService {
    private final VisitRepository repository;
    private final WaitingListService waitingListService;
    private final VisitorService visitorService;
    private final VisitMapper mapper;

    @Override
    public VisitResponseDto subscribeVisitor(WaitingListId waitingListId, VisitorId visitorId, VisitRequestDto dto) {
        Visitor visitor = visitorService.findEntityById(visitorId);
        WaitingList waitingList = waitingListService.findEntityById(waitingListId);

        if (waitingList.getDate().isBefore(LocalDate.now()))
            throw new WaitingListDatePassedException("You can't subscribe to a waiting list that has already passed");

        if (repository.existsById(new VisitId(visitorId, waitingListId)))
            throw new DuplicateSubscriptionException("You have already subscribed to this waiting list");

        Visit savedVisit = repository.save(
                new Visit(visitor, waitingList, dto.priority(), dto.estimatedProcessingTim()));

        return mapper.toResponseDto(savedVisit);
    }

    @Override
    public VisitResponseDto cancelSubscription(WaitingListId waitingListId, VisitorId visitorId) {
        Visit visit = findVisit(waitingListId, visitorId)
                .cancelVisit();
        return mapper.toResponseDto(visit);
    }

    @Override
    public VisitResponseDto beginVisit(WaitingListId waitingListId, VisitorId visitorId) {
        Visit visit = findVisit(waitingListId, visitorId)
                .beginVisit();
        return mapper.toResponseDto(visit);
    }

    @Override
    public VisitResponseDto completeVisit(WaitingListId waitingListId, VisitorId visitorId) {
        Visit visit = findVisit(waitingListId, visitorId)
                .completeVisit();
        return mapper.toResponseDto(visit);
    }

    private Visit findVisit(WaitingListId waitingListId, VisitorId visitorId) {
        return repository.findById(new VisitId(visitorId, waitingListId))
                .orElseThrow(() -> new EntityNotFoundException("there is no visit for waiting list id: " + waitingListId.value() + " and visitor id: " + visitorId.value()));
    }
}
