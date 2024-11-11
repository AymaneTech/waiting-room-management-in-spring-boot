package com.wora.waitingRoom.waitingList.application.mapper;
import  com.wora.waitingRoom.common.application.mapper.BaseMapper;
import com.wora.waitingRoom.waitingList.application.dto.request.CreateWaitingListRequestDto;
import com.wora.waitingRoom.waitingList.application.dto.response.WaitingListResponseDto;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;

import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface WaitingListMapper extends BaseMapper<WaitingList, CreateWaitingListRequestDto, WaitingListResponseDto>{
}
