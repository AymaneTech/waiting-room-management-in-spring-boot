package com.wora.waitingRoom.visitor.application.service;

import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.visitor.domain.VisitorId;

public interface VisitorService {
    Visitor findEntityById(VisitorId id);
}
