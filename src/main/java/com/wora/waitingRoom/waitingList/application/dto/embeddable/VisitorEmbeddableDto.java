package com.wora.waitingRoom.waitingList.application.dto.embeddable;

import jakarta.validation.constraints.NotNull;

public record VisitorEmbeddableDto(
        @NotNull Long id,
        @NotNull String firstName,
        @NotNull String lastName
) {
}
