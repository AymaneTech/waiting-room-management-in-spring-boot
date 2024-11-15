package com.wora.waitingroom.visitor.application.service;

import com.wora.waitingroom.visitor.domain.Visitor;
import com.wora.waitingroom.visitor.domain.VisitorId;

public interface VisitorService {
    Visitor findEntityById(VisitorId id);
}
