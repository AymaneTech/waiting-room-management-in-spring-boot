package com.wora.waitingRoom.waitingList.application.mapper;

import com.wora.waitingRoom.common.application.mapper.BaseMapper;
import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = BaseMapper.class)
public interface VisitMapper {
    @Mappings({
            @Mapping(target = "visitor.firstName", source = "visitor.name.firstName"),
            @Mapping(target = "visitor.lastName", source = "visitor.name.lastName")
    })
    VisitResponseDto toResponseDto(Visit visit);
}
