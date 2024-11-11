package com.wora.waitingRoom.waitingList.application.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;

public record CreateWaitingList(@NotNull LocalDate date,
        Integer capacity,
        Mode mode,
        Algorithm algorithm) {
}
