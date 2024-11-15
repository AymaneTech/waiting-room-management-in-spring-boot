package com.wora.waitingroom.visitor.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, VisitorId> {

}
