package com.wora.waitingRoom.waitingList.application.service;

import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
import org.springframework.data.domain.Page;

public interface VisitsSchedulingService {
    Page<VisitResponseDto> scheduleVisits(WaitingListId waitingListId);

    Page<VisitResponseDto> scheduleVisitsByStatus(WaitingListId waitingListId, Status status);
}
