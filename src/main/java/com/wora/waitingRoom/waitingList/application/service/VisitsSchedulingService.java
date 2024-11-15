package com.wora.waitingRoom.waitingList.application.service;

import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;
import org.springframework.data.domain.Page;

public interface VisitsSchedulingService {
    Page<VisitResponseDto> scheduleVisits(VisitId visitId);
}
