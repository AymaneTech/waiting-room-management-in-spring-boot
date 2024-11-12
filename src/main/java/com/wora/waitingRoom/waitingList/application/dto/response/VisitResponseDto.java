package com.wora.waitingRoom.waitingList.application.dto.response;

import com.wora.waitingRoom.waitingList.application.dto.embeddable.VisitorEmbeddableDto;
import com.wora.waitingRoom.waitingList.application.dto.embeddable.WaitingListEmbeddableDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record VisitResponseDto(@NotNull Long id,
                               @NotNull LocalTime arrivalTime,
                               LocalTime startTime,
                               LocalTime endDate,
                               @NotNull String status,
                               Byte priority,
                               Long estimatedProcessingTime,
                               VisitorEmbeddableDto visitor,
                               WaitingListEmbeddableDto waitingList
) {
}
