package com.wora.waitingRoom.waitingList.domain.repository;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, VisitId> {

    @Query("SELECT e FROM Visit e WHERE e.id = :id AND e.status = :status")
    List<Visit> findAllByWaitingListIdAndStatus(VisitId id, Status status);
}
