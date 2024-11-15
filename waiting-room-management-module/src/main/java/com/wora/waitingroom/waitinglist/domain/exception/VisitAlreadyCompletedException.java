package com.wora.waitingroom.waitinglist.domain.exception;

public class VisitAlreadyCompletedException extends RuntimeException {
    public VisitAlreadyCompletedException(String message) {
        super(message);
    }
}
