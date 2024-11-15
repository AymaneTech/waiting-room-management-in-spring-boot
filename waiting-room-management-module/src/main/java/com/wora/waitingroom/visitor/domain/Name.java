package com.wora.waitingroom.visitor.domain;

import jakarta.validation.constraints.NotBlank;

public record Name(@NotBlank String firstName,
        @NotBlank String lastName) {
}
