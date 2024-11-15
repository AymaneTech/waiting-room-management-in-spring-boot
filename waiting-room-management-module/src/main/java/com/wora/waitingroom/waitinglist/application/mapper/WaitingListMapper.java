package com.wora.waitingroom.waitinglist.application.mapper;
import com.wora.waitingroom.common.application.mapper.BaseMapper;
import com.wora.waitingroom.waitinglist.application.dto.request.WaitingListRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.WaitingListResponseDto;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;

import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface WaitingListMapper extends BaseMapper<WaitingList, WaitingListRequestDto, WaitingListResponseDto>{
}
