package com.wora.waitingroom.common.application.service;

import org.springframework.data.domain.Page;

public interface CrudService<ID, REQUEST_DTO, RESPONSE_DTO> {
    Page<RESPONSE_DTO> findAll(int pageNum, int pageSize);

    RESPONSE_DTO findById(ID id);

    RESPONSE_DTO create(REQUEST_DTO dto);

    RESPONSE_DTO update(ID id, REQUEST_DTO dto);

    void delete(ID id);
}
