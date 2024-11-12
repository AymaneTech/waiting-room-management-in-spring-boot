package com.wora.waitingRoom.waitingList.application.mapper;

import com.wora.waitingRoom.common.application.mapper.BaseMapper;
import com.wora.waitingRoom.waitingList.application.dto.request.VisitRequestDto;
import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface VisitMapper extends BaseMapper<Visit, VisitRequestDto, VisitResponseDto> {
}
