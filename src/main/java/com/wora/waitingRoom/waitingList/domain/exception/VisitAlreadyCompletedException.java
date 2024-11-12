package com.wora.waitingRoom.waitingList.domain.exception;

public class VisitAlreadyCompletedException extends RuntimeException {
    public VisitAlreadyCompletedException(String message) {
        super(message);
    }
}
