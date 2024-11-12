package com.wora.waitingRoom.waitingList.application.service;

import com.wora.waitingRoom.visitor.domain.VisitorId;
import com.wora.waitingRoom.waitingList.application.dto.request.VisitRequestDto;
import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;

public interface VisitService {
    VisitResponseDto subscribeVisitor(WaitingListId waitingListId, VisitorId visitorId, VisitRequestDto dto);

    VisitResponseDto cancelSubscription(WaitingListId waitingListId, VisitorId visitorId);

    VisitResponseDto beginVisit(WaitingListId waitingListId, VisitorId visitorId);

    VisitResponseDto completeVisit(WaitingListId waitingListId, VisitorId visitorId);
}
