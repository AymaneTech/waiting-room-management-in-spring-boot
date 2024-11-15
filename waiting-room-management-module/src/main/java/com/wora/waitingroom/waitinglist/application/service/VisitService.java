package com.wora.waitingroom.waitinglist.application.service;

import com.wora.waitingroom.visitor.domain.VisitorId;
import com.wora.waitingroom.waitinglist.application.dto.request.VisitRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.VisitResponseDto;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;

public interface VisitService {
    VisitResponseDto subscribeVisitor(WaitingListId waitingListId, VisitorId visitorId, VisitRequestDto dto);

    VisitResponseDto cancelSubscription(WaitingListId waitingListId, VisitorId visitorId);

    VisitResponseDto beginVisit(WaitingListId waitingListId, VisitorId visitorId);

    VisitResponseDto completeVisit(WaitingListId waitingListId, VisitorId visitorId);
}
