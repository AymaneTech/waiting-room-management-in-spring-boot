package com.wora.waitingroom.waitinglist.application.dto.response;

import com.wora.waitingroom.waitinglist.application.dto.embeddable.VisitorEmbeddableDto;
import com.wora.waitingroom.waitinglist.application.dto.embeddable.WaitingListEmbeddableDto;
import com.wora.waitingroom.waitinglist.domain.vo.Status;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalTime;

public record VisitResponseDto(@NotNull LocalTime arrivalTime,
                               LocalTime startTime,
                               LocalTime endTime,
                               @NotNull Status status,
                               Byte priority,
                               Duration estimatedProcessingTime,
                               VisitorEmbeddableDto visitor,
                               WaitingListEmbeddableDto waitingList
) {
}
