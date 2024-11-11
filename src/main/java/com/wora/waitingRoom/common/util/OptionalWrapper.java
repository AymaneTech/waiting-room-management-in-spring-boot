package com.wora.waitingRoom.common.util;

import java.util.Optional;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;

public class OptionalWrapper {
    public static <T> T orElseThrow(Optional<T> optional, String entity, Object id) {
        return optional.orElseThrow(() -> new EntityNotFoundException(entity, id));
    }
    
    public static <T> T orElseThrow(Optional<T> optional, String message) {
        return optional.orElseThrow(() -> new EntityNotFoundException(message));
    }
    
}
