package com.wora.waitingRoom.visitor.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record VisitorId(@GeneratedValue Long value) {
}
