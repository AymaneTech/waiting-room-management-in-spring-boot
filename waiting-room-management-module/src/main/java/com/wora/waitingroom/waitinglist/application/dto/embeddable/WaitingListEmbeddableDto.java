package com.wora.waitingroom.waitinglist.application.dto.embeddable;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.Mode;

public record WaitingListEmbeddableDto(@NotNull Long id,
                                       @NotNull LocalDate date,
                                       @NotNull Integer capacity,
                                       @NotNull Mode mode,
                                       @NotNull Algorithm algorithm) {
}
