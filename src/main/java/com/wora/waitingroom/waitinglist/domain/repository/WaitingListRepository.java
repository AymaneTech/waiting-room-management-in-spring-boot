package com.wora.waitingroom.waitinglist.domain.repository;

import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingListRepository extends JpaRepository<WaitingList, WaitingListId>{
}
