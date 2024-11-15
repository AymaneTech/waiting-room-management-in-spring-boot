package com.wora.waitingroom.waitinglist.application.mapper;

import com.wora.waitingroom.common.application.mapper.BaseMapper;
import com.wora.waitingroom.waitinglist.application.dto.response.VisitResponseDto;
import com.wora.waitingroom.waitinglist.domain.entity.Visit;
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
