package com.wora.waitingroom.waitinglist.application.dto.request;

import java.time.Duration;

public record VisitRequestDto(Byte priority,
                              Duration estimatedProcessingTim) {
}
