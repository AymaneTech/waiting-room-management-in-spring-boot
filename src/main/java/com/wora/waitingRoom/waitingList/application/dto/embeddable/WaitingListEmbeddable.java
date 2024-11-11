package com.wora.waitingRoom.waitingList.application.dto.embeddable;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;

public record WaitingListEmbeddable(@NotNull Long id,
        @NotNull LocalDate date,
        @NotNull Integer capacity,
        @NotNull Mode mode,
        @NotNull Algorithm algorithm) {
}
