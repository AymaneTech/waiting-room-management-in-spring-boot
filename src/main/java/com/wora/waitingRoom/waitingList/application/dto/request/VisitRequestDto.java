package com.wora.waitingRoom.waitingList.application.dto.request;

import java.time.Duration;

public record VisitRequestDto(Byte priority,
                              Duration estimatedProcessingTim) {
}
