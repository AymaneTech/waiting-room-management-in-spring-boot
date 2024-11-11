package com.wora.waitingRoom.waitingList.domain.repository;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, VisitId>{
}
