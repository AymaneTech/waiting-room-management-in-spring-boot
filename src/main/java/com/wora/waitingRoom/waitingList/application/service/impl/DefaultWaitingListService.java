package com.wora.waitingRoom.waitingList.application.service.impl;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.waitingList.application.dto.request.WaitingListRequestDto;
import com.wora.waitingRoom.waitingList.application.dto.response.WaitingListResponseDto;
import com.wora.waitingRoom.waitingList.application.mapper.WaitingListMapper;
import com.wora.waitingRoom.waitingList.application.service.WaitingListService;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.repository.WaitingListRepository;
import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
import com.wora.waitingRoom.waitingList.infrastructure.WaitingListConfigurationValues;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

import static com.wora.waitingRoom.common.util.OptionalWrapper.orElseThrow;

@Service
@Transactional
@Validated
public class DefaultWaitingListService implements WaitingListService {
    private final WaitingListRepository repository;
    private final WaitingListMapper mapper;
    private final WaitingListConfigurationValues configurationProperties;

    public DefaultWaitingListService(WaitingListRepository repository, WaitingListMapper mapper, WaitingListConfigurationValues configurationProperties) {
        this.repository = repository;
        this.mapper = mapper;
        this.configurationProperties = configurationProperties;
    }

    @Override
    public List<WaitingListResponseDto> findAll() {
        return repository.findAll()
                .stream().map(mapper::toResponseDto).toList();
    }

    @Override
    public WaitingListResponseDto findById(WaitingListId id) {
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("waiting list", id.value()));
    }

    @Override
    public WaitingListResponseDto create(WaitingListRequestDto dto) {
        System.out.println(configurationProperties);
        WaitingList waitingList = mapper.toEntity(dto);

        Optional.ofNullable(dto.capacity())
                .ifPresentOrElse(waitingList::setCapacity,
                        () -> waitingList.setCapacity(configurationProperties.capacity()));

        Optional.ofNullable(dto.mode())
                .ifPresentOrElse(waitingList::setMode,
                        () -> waitingList.setMode(configurationProperties.mode()));

        Optional.ofNullable(dto.algorithm())
                .ifPresentOrElse(waitingList::setAlgorithm,
                        () -> waitingList.setAlgorithm(configurationProperties.algorithm()));

        WaitingList savedWaitingList = repository.save(waitingList);
        return mapper.toResponseDto(savedWaitingList);
    }

    @Override
    public WaitingListResponseDto update(WaitingListId id, WaitingListRequestDto dto) {
        WaitingList waitingList = orElseThrow(repository.findById(id), "waiting list", id.value());

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
