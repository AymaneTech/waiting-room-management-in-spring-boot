package com.wora.waitingRoom.common.application.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {IdValueObjectMapper.class}
)
public interface BaseMapper<Entity, Request, Response> {
    Entity toEntity(Request dto);

    Response toResponseDto(Entity entity);
}
