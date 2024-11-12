package com.wora.waitingRoom.common.application.service;

import org.springframework.data.domain.Page;

import java.util.List;

public interface CrudService<Id, RequestDto, ResponseDto> {
    Page<ResponseDto> findAll(int pageNum, int pageSize);

    ResponseDto findById(Id id);

    ResponseDto create(RequestDto dto);

    ResponseDto update(Id id, RequestDto dto);

    void delete(Id id);
}
