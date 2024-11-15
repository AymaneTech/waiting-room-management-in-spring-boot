package com.wora.waitingRoom.waitingList.application.service;

import com.wora.waitingroom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingroom.visitor.domain.Visitor;
import com.wora.waitingroom.waitinglist.application.dto.response.VisitResponseDto;
import com.wora.waitingroom.waitinglist.application.mapper.VisitMapper;
import com.wora.waitingroom.waitinglist.application.service.VisitsSchedulingService;
import com.wora.waitingroom.waitinglist.application.service.impl.DefaultVisitsSchedulingService;
import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.repository.VisitRepository;
import com.wora.waitingroom.waitinglist.domain.repository.WaitingListRepository;
import com.wora.waitingroom.waitinglist.domain.service.Scheduler;
import com.wora.waitingroom.waitinglist.domain.service.SchedulerFactory;
import com.wora.waitingroom.waitinglist.domain.service.impl.FifoScheduler;
import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.Mode;
import com.wora.waitingroom.waitinglist.domain.vo.Status;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    private SchedulerFactory schedulerFactory;
    @Mock
    private VisitMapper mapper;
    private Scheduler scheduler;
    private VisitsSchedulingService underTest;

    private WaitingList waitingList;


    @BeforeEach
    void setup() {
        this.underTest = new DefaultVisitsSchedulingService(repository, waitingListRepository, schedulerFactory, mapper);
        this.scheduler = new FifoScheduler();
        waitingList = WaitingList.builder()
                .id(new WaitingListId(2L))
                .date(LocalDate.now())
                .capacity(30)
                .mode(Mode.FULL_TIME)
                .algorithm(Algorithm.FIFO)
                .build();
    }

    @Test
    void givenEmptyVisitsList_whenScheduleVisits_thenReturnEmptyList() {
        given(waitingListRepository.findById(any(WaitingListId.class))).willReturn(Optional.of(waitingList));
        given(repository.findAllByWaitingListIdAndStatus(waitingList.getId(), Status.WAITING)).willReturn(List.of());
        given(schedulerFactory.getScheduler(any(Algorithm.class))).willReturn(scheduler);

        Page<VisitResponseDto> actual = underTest.scheduleVisits(waitingList.getId());

        assertThat(actual.isEmpty()).isTrue();
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

        given(waitingListRepository.findById(waitingList.getId())).willReturn(Optional.of(waitingList));
        given(repository.findAllByWaitingListIdAndStatus(waitingList.getId(), Status.WAITING)).willReturn(visits);
        given(schedulerFactory.getScheduler(any(Algorithm.class))).willReturn(scheduler);

        Page<VisitResponseDto> actual = underTest.scheduleVisits(waitingList.getId());

        assertThat(actual.getTotalElements()).isEqualTo(4);
    }

    @Test
    void givenNotExistsVisitId_whenScheduleVisits_thenThrowEntityNotFoundException() {
        given(waitingListRepository.findById(waitingList.getId())).willReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> underTest.scheduleVisits(waitingList.getId()))
                .withMessage("waiting list with id 2 not found");
    }
}