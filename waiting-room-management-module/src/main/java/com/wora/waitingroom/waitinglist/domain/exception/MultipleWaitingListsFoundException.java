package com.wora.waitingroom.waitinglist.domain.exception;

public class MultipleWaitingListsFoundException extends RuntimeException{
    public MultipleWaitingListsFoundException(String message) {
        super(message);
    }
}
