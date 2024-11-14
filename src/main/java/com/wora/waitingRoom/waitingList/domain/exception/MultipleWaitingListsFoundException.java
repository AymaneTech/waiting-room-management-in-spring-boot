package com.wora.waitingRoom.waitingList.domain.exception;

public class MultipleWaitingListsFoundException extends RuntimeException{
    public MultipleWaitingListsFoundException(String message) {
        super(message);
    }
}
