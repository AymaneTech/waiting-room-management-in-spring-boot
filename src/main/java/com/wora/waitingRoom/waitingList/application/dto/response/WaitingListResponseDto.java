package com.wora.waitingRoom.waitingList.application.dto.response;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;

public record WaitingListResponseDto(@NotNull Long id,
        @NotNull LocalDate date,
        @NotNull Integer capacity,
        @NotNull Mode mode,
        @NotNull Algorithm algorithm) {
}
