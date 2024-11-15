package com.wora.waitingroom.waitinglist.application.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.Mode;
import jakarta.validation.constraints.Positive;

public record WaitingListRequestDto(@NotNull @FutureOrPresent LocalDate date,
                                    @Positive Integer capacity,
                                    Mode mode,
                                    Algorithm algorithm) {
}
