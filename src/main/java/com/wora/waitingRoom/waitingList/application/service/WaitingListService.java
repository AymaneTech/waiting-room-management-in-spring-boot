package com.wora.waitingRoom.waitingList.application.service;

import com.wora.waitingRoom.common.application.service.CrudService;
import com.wora.waitingRoom.waitingList.application.dto.request.WaitingListRequestDto;
import com.wora.waitingRoom.waitingList.application.dto.response.WaitingListResponseDto;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;

public interface WaitingListService
        extends CrudService<WaitingListId, WaitingListRequestDto, WaitingListResponseDto> {
}
