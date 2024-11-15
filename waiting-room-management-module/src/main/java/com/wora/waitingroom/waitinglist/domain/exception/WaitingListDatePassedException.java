package com.wora.waitingroom.waitinglist.domain.exception;

public class WaitingListDatePassedException extends RuntimeException {
    public WaitingListDatePassedException(String message) {
        super(message);
    }
}
