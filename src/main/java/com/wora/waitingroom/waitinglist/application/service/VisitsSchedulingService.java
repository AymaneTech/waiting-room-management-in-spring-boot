package com.wora.waitingroom.waitinglist.application.service;

import com.wora.waitingroom.waitinglist.application.dto.response.VisitResponseDto;
import com.wora.waitingroom.waitinglist.domain.vo.Status;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import org.springframework.data.domain.Page;

public interface VisitsSchedulingService {
    Page<VisitResponseDto> scheduleVisits(WaitingListId waitingListId);

    Page<VisitResponseDto> scheduleVisitsByStatus(WaitingListId waitingListId, Status status);
}
