package com.wora.waitingRoom.waitingList.domain.repository;

import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import com.wora.waitingRoom.waitingList.domain.valueObject.VisitId;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, VisitId> {

    @Query("SELECT e FROM Visit e WHERE e.waitingList.id = :id AND e.status = :status")
    List<Visit> findAllByWaitingListIdAndStatus(WaitingListId id, Status status);
}
