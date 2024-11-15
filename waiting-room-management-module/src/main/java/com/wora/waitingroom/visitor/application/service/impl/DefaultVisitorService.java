package com.wora.waitingroom.visitor.application.service.impl;

import com.wora.waitingroom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingroom.visitor.application.service.VisitorService;
import com.wora.waitingroom.visitor.domain.Visitor;
import com.wora.waitingroom.visitor.domain.VisitorId;
import com.wora.waitingroom.visitor.domain.VisitorRepository;
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
