package com.wora.waitingroom.waitinglist.domain.repository;

import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.vo.Status;
import com.wora.waitingroom.waitinglist.domain.vo.VisitId;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, VisitId> {

    @Query("SELECT e FROM Visit e WHERE e.waitingList.id = :id AND e.status = :status")
    List<Visit> findAllByWaitingListIdAndStatus(WaitingListId id, Status status);
}
