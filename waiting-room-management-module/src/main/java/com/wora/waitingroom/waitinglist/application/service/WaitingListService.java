package com.wora.waitingroom.waitinglist.application.service;

import com.wora.waitingroom.common.application.service.CrudService;
import com.wora.waitingroom.waitinglist.application.dto.request.WaitingListRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.WaitingListResponseDto;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;

public interface WaitingListService extends CrudService<WaitingListId, WaitingListRequestDto, WaitingListResponseDto> {
    WaitingList findEntityById(WaitingListId id);
}
