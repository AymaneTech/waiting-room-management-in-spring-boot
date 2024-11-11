package com.wora.waitingRoom.waitingList.application.service.impl;

import java.util.List;
import java.util.Optional;

import com.wora.waitingRoom.waitingList.application.dto.request.WaitingListRequestDto;
import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import jakarta.transaction.Transactional;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.waitingList.application.dto.response.WaitingListResponseDto;
import com.wora.waitingRoom.waitingList.application.mapper.WaitingListMapper;
import com.wora.waitingRoom.waitingList.application.service.WaitingListService;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.repository.WaitingListRepository;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;

import org.springdoc.core.converters.ResponseSupportConverter;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;

import static com.wora.waitingRoom.common.util.OptionalWrapper.orElseThrow;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class DefaultWaitingListService implements WaitingListService {
    private final WaitingListRepository repository;
    private final WaitingListMapper mapper;
    private final ResponseSupportConverter responseSupportConverter;

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
        WaitingList waitingList = mapper.toEntity(dto);

        Optional.ofNullable(dto.capacity())
                .ifPresentOrElse(waitingList::setCapacity,
                        () -> waitingList.setCapacity(9393));

        Optional.ofNullable(dto.mode())
                .ifPresentOrElse(waitingList::setMode,
                        () -> waitingList.setMode(Mode.PART_TIME));

        Optional.ofNullable(dto.algorithm())
            .ifPresentOrElse(
                    waitingList::setAlgorithm,
                    () -> waitingList.setAlgorithm(Algorithm.FIFO));

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
            throw new EntityNotFoundException("waiting list",  id.value());

        repository.deleteById(id);
    }

}
