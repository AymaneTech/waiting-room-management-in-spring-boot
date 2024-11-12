package com.wora.waitingRoom.waitingList.application.dto.response;

import com.wora.waitingRoom.waitingList.application.dto.embeddable.VisitorEmbeddableDto;
import com.wora.waitingRoom.waitingList.application.dto.embeddable.WaitingListEmbeddableDto;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.time.LocalTime;

public record VisitResponseDto(@NotNull LocalTime arrivalTime,
                               LocalTime startTime,
                               LocalTime endDate,
                               @NotNull Status status,
                               Byte priority,
                               Duration estimatedProcessingTime,
                               VisitorEmbeddableDto visitor,
                               WaitingListEmbeddableDto waitingList
) {
}
