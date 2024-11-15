package com.wora.waitingroom.waitinglist.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;

@Embeddable
public record WaitingListId(@GeneratedValue Long value) {
}
