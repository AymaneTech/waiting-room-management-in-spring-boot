package com.wora.waitingroom.waitinglist.application.service.impl;

import com.wora.waitingroom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingroom.visitor.application.service.VisitorService;
import com.wora.waitingroom.visitor.domain.Visitor;
import com.wora.waitingroom.visitor.domain.VisitorId;
import com.wora.waitingroom.waitinglist.application.dto.request.VisitRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.VisitResponseDto;
import com.wora.waitingroom.waitinglist.application.mapper.VisitMapper;
import com.wora.waitingroom.waitinglist.application.service.VisitService;
import com.wora.waitingroom.waitinglist.application.service.WaitingListService;
import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.exception.DuplicateSubscriptionException;
import com.wora.waitingroom.waitinglist.domain.exception.WaitingListDatePassedException;
import com.wora.waitingroom.waitinglist.domain.repository.VisitRepository;
import com.wora.waitingroom.waitinglist.domain.vo.VisitId;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
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
