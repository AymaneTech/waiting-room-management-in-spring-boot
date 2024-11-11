package com.wora.waitingRoom.waitingList.domain.valueObject;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record VisitId(@GeneratedValue Long value) {
}
