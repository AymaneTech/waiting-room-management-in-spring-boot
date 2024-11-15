package com.wora.waitingRoom.waitingList.application.service;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.application.mapper.VisitMapper;
import com.wora.waitingRoom.waitingList.application.service.impl.DefaultVisitsSchedulingService;
import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.repository.VisitRepository;
import com.wora.waitingRoom.waitingList.domain.repository.WaitingListRepository;
import com.wora.waitingRoom.waitingList.domain.service.Scheduler;
import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import com.wora.waitingRoom.waitingList.domain.valueObject.Status;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class DefaultVisitsSchedulingServiceTest {
    @Mock
    private VisitRepository repository;
    @Mock
    private WaitingListRepository waitingListRepository;
    @Mock
    private Scheduler scheduler;
    @Mock
    private VisitMapper mapper;
    private VisitsSchedulingService underTest;

    private WaitingListId waitingListId;


    @BeforeEach
    void setup() {
        this.underTest = new DefaultVisitsSchedulingService(repository, waitingListRepository, scheduler, mapper);
        waitingListId = new WaitingListId(2L);
    }

    @Test
    void givenEmptyVisitsList_whenScheduleVisits_thenReturnEmptyList() {
        given(waitingListRepository.existsById(waitingListId)).willReturn(true);
        given(repository.findAllByWaitingListIdAndStatus(waitingListId, Status.WAITING)).willReturn(List.of());
        given(scheduler.schedule(List.of())).willReturn(List.of());

        Page<VisitResponseDto> actual = underTest.scheduleVisits(waitingListId);

        assertThat(actual.isEmpty()).isTrue();
        verify(scheduler).schedule(List.of());
        verify(mapper, never()).toResponseDto(any(Visit.class));
    }

    @Test
    void givenVisits_whenScheduleVisits_thenReturnVisitsScheduled() {
        WaitingList waitingList = WaitingList.builder()
                .id(new WaitingListId(3L))
                .date(LocalDate.now())
                .capacity(10)
                .mode(Mode.PART_TIME)
                .algorithm(Algorithm.HPF)
                .build();
        List<Visitor> visitors = List.of(
                new Visitor(1L, "yahya", "el maini"),
                new Visitor(2L, "abdelhak", "azrour"),
                new Visitor(3L, "hamza", "lamin"),
                new Visitor(4L, "soufiane", "bouanani"));
        List<Visit> visits = visitors.stream()
                .map(v -> new Visit(v, waitingList, null, null))
                .toList();

        given(waitingListRepository.existsById(waitingListId)).willReturn(true);
        given(repository.findAllByWaitingListIdAndStatus(waitingListId, Status.WAITING)).willReturn(visits);
        given(scheduler.schedule(visits)).willReturn(visits);

        Page<VisitResponseDto> actual = underTest.scheduleVisits(waitingListId);

        assertThat(actual.getTotalElements()).isEqualTo(4);
    }

    @Test
    void givenNotExistsVisitId_whenScheduleVisits_thenThrowEntityNotFoundException() {
        given(waitingListRepository.existsById(waitingListId)).willReturn(false);

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> underTest.scheduleVisits(waitingListId))
                .withMessage("waiting list with id 2 not found");
    }
}