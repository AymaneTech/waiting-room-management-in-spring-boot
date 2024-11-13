package com.wora.waitingRoom.waitingList.application.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import jakarta.validation.constraints.Positive;

public record WaitingListRequestDto(@NotNull @FutureOrPresent LocalDate date,
                                    @Positive Integer capacity,
                                    Mode mode,
                                    Algorithm algorithm) {
}
