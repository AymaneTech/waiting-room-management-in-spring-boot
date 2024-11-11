package com.wora.waitingRoom.waitingList.domain.repository;

import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingListRepository extends JpaRepository<WaitingList, WaitingListId>{
}
