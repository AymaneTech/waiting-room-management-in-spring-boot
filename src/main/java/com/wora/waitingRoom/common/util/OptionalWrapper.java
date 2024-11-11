package com.wora.waitingRoom.common.util;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;

import java.util.Optional;

public class OptionalWrapper {
    public static <T> T orElseThrow(Optional<T> optional, String entity, Object id) {
        return optional.orElseThrow(() -> new EntityNotFoundException(entity, id));
    }

}
