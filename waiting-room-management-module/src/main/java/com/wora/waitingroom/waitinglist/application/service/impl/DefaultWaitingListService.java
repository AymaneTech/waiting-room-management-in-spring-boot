package com.wora.waitingroom.waitinglist.application.service.impl;

import com.wora.waitingroom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingroom.config.configurationProperties.WaitingListConfigurationProperties;
import com.wora.waitingroom.waitinglist.application.dto.request.WaitingListRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.WaitingListResponseDto;
import com.wora.waitingroom.waitinglist.application.mapper.WaitingListMapper;
import com.wora.waitingroom.waitinglist.application.service.WaitingListService;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.repository.WaitingListRepository;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.wora.waitingroom.common.util.OptionalWrapper.orElseThrow;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class DefaultWaitingListService implements WaitingListService {
    private final WaitingListRepository repository;
    private final WaitingListMapper mapper;
    private final WaitingListConfigurationProperties configurationProperties;

    @Override
    public Page<WaitingListResponseDto> findAll(int pageNum, int pageSize) {
        return repository.findAll(PageRequest.of(pageNum, pageSize))
                .map(mapper::toResponseDto);
    }

    @Override
    public WaitingListResponseDto findById(WaitingListId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("waiting list", id.value()));
    }

    @Override
    public WaitingList findEntityById(WaitingListId id) {
        return orElseThrow(repository.findById(id), "waiting list", id.value());
    }

    @Override
    public WaitingListResponseDto create(WaitingListRequestDto dto) {
        WaitingList waitingList = WaitingList.builder()
                .date(dto.date())
                .capacity(dto.capacity(), configurationProperties.capacity())
                .mode(dto.mode(), configurationProperties.mode())
                .algorithm(dto.algorithm(), configurationProperties.algorithm())
                .build();

        WaitingList savedWaitingList = repository.save(waitingList);
        return mapper.toResponseDto(savedWaitingList);
    }

    @Override
    public WaitingListResponseDto update(WaitingListId id, WaitingListRequestDto dto) {
        WaitingList waitingList = findEntityById(id);

        waitingList.setDate(dto.date())
                .setCapacity(dto.capacity())
                .setMode(dto.mode())
                .setAlgorithm(dto.algorithm());

        return mapper.toResponseDto(waitingList);
    }

    @Override
    public void delete(WaitingListId id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("waiting list", id.value());

        repository.deleteById(id);
    }
}
