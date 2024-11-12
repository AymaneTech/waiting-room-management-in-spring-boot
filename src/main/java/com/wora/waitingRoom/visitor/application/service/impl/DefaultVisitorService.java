package com.wora.waitingRoom.visitor.application.service.impl;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.visitor.application.service.VisitorService;
import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.visitor.domain.VisitorId;
import com.wora.waitingRoom.visitor.domain.VisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultVisitorService implements VisitorService {
    private final VisitorRepository repository;

    @Override
    public Visitor findEntityById(VisitorId id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("visitor", id.value()));
    }
}
