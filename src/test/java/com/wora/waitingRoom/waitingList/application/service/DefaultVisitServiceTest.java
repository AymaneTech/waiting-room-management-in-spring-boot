package com.wora.waitingRoom.waitingList.application.service;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.visitor.application.service.VisitorService;
import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.waitingList.application.dto.request.VisitRequestDto;
import com.wora.waitingRoom.waitingList.application.dto.response.VisitResponseDto;
import com.wora.waitingRoom.waitingList.application.mapper.VisitMapper;
import com.wora.waitingRoom.waitingList.application.service.impl.DefaultVisitService;
import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.exception.DuplicateSubscriptionException;
import com.wora.waitingRoom.waitingList.domain.exception.VisitAlreadyCanceledException;
import com.wora.waitingRoom.waitingList.domain.exception.VisitAlreadyCompletedException;
import com.wora.waitingRoom.waitingList.domain.exception.WaitingListDatePassedException;
import com.wora.waitingRoom.waitingList.domain.repository.VisitRepository;
import com.wora.waitingRoom.waitingList.domain.valueObject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class DefaultVisitServiceTest {
    @Mock
    private VisitRepository visitRepository;
    @Mock
    private WaitingListService waitingListService;
    @Mock
    private VisitorService visitorService;
    @Mock
    private VisitMapper mapper;

    private VisitService underTest;
    private WaitingList waitingList;
    private Visitor visitor;

    @BeforeEach
    void setup() {
        underTest = new DefaultVisitService(visitRepository, waitingListService, visitorService, mapper);
        waitingList = WaitingList.builder()
                .id(new WaitingListId(3L))
                .date(LocalDate.now())
                .capacity(10)
                .mode(Mode.PART_TIME)
                .algorithm(Algorithm.HPF)
                .build();
        visitor = new Visitor(2L, "aymane", "the goat");
    }

    @Nested
    class SubscribeVisitorTests {
        @Test
        void givenWaitingListIdDoesNotExists_whenSubscribeVisitor_thenThrowEntityNotFoundException() {
            VisitRequestDto request = new VisitRequestDto(null, null);

            given(waitingListService.findEntityById(waitingList.getId())).willThrow(new EntityNotFoundException("waiting list with id 3 not found"));

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.subscribeVisitor(waitingList.getId(), visitor.getId(), request))
                    .withMessageContaining("waiting list with id 3 not found");
        }

        @Test
        void givenVisitorIdDoesNotExists_whenSubscribeVisitor_thenThrowEntityNotFoundException() {
            VisitRequestDto request = new VisitRequestDto(null, null);

            given(waitingListService.findEntityById(waitingList.getId())).willReturn(waitingList);
            given(visitorService.findEntityById(visitor.getId())).willThrow(new EntityNotFoundException("visitor with id 2 not found"));

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.subscribeVisitor(waitingList.getId(), visitor.getId(), request))
                    .withMessageContaining("visitor with id 2 not found");
        }

        @Test
        void givenWaitingListDatePassed_whenSubscribeVisitor_thenThrowWaitingListDatePassedException() {
            VisitRequestDto request = new VisitRequestDto(null, null);
            waitingList.setDate(LocalDate.now().minusDays(1));

            given(visitorService.findEntityById(visitor.getId())).willReturn(visitor);
            given(waitingListService.findEntityById(waitingList.getId())).willReturn(waitingList);

            assertThatExceptionOfType(WaitingListDatePassedException.class)
                    .isThrownBy(() -> underTest.subscribeVisitor(waitingList.getId(), visitor.getId(), request))
                    .withMessageContaining("You can't subscribe to a waiting list that has already passed");
        }

        @Test
        void givenWaitingListAlreadyExists_whenSubscribeVisitor_thenThrowDuplicateSubscriptionException() {
            VisitRequestDto request = new VisitRequestDto(null, null);

            given(visitorService.findEntityById(visitor.getId())).willReturn(visitor);
            given(waitingListService.findEntityById(waitingList.getId())).willReturn(waitingList);
            given(visitRepository.existsById(any(VisitId.class))).willReturn(true);

            assertThatExceptionOfType(DuplicateSubscriptionException.class)
                    .isThrownBy(() -> underTest.subscribeVisitor(waitingList.getId(), visitor.getId(), request))
                    .withMessageContaining("You have already subscribed to this waiting list");
        }

        @Test
        void givenVisitRequestCorrect_whenSubscribeVisitor_thenReturnVisitResponseDto() {
            VisitRequestDto request = new VisitRequestDto(null, null);
            Visit visit = new Visit(visitor, waitingList, null, null);

            given(visitorService.findEntityById(visitor.getId())).willReturn(visitor);
            given(waitingListService.findEntityById(waitingList.getId())).willReturn(waitingList);
            given(visitRepository.existsById(any(VisitId.class))).willReturn(false);
            given(visitRepository.save(any(Visit.class))).willReturn(visit);
            given(mapper.toResponseDto(visit)).willAnswer(invocation -> {
                Visit v = invocation.getArgument(0);
                return new VisitResponseDto(v.getArrivalTime(), v.getStartTime(), v.getEndDate(), v.getStatus(), v.getPriority(), v.getEstimatedProcessingTime(), null, null);
            });

            VisitResponseDto actual = underTest.subscribeVisitor(waitingList.getId(), visitor.getId(), request);

            assertThat(actual).isNotNull();
            assertThat(actual.arrivalTime()).isEqualTo(visit.getArrivalTime());
        }
    }

    @Nested
    class CancelSubscriptionTests {
        @Test
        void givenVisitDoesNotExists_whenCancelSubscription_thenThrowEntityNotFoundException() {
            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.cancelSubscription(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("there is no visit for waiting list id: 3 and visitor id: 2");
        }

        @Test
        void givenVisitIsAlreadyCanceled_whenCancelSubscription_thenThrowVisitAlreadyCompletedException() {
            Visit visit = new Visit(visitor, waitingList, null, null);
            visit.cancelVisit();

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));

            assertThatExceptionOfType(VisitAlreadyCompletedException.class)
                    .isThrownBy(() -> underTest.cancelSubscription(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("Visit has already been canceled");
        }

        @Test
        void givenVisitIsAlreadyInProgress_whenCancelSubscription_thenThrowVisitAlreadyCompletedException() {
            Visit visit = new Visit(visitor, waitingList, null, null);
            visit.beginVisit();

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));

            assertThatExceptionOfType(VisitAlreadyCompletedException.class)
                    .isThrownBy(() -> underTest.cancelSubscription(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("Visit has already been in_progress");
        }

        @Test
        void givenVisit_whenCancelSubscription_thenReturnVisitResponseDto() {
            Visit visit = new Visit(visitor, waitingList, null, null);

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));
            given(mapper.toResponseDto(visit)).willAnswer(invocation -> {
                Visit v = invocation.getArgument(0);
                return new VisitResponseDto(v.getArrivalTime(), v.getStartTime(), v.getEndDate(), v.getStatus(), v.getPriority(), v.getEstimatedProcessingTime(), null, null);
            });

            VisitResponseDto actual = underTest.cancelSubscription(waitingList.getId(), visitor.getId());

            assertThat(actual).isNotNull();
            assertThat(actual.arrivalTime()).isEqualTo(visit.getArrivalTime());
            assertThat(actual.status()).isEqualTo(Status.CANCELED);
            assertThat(actual.startTime()).isNull();
            assertThat(actual.endTime()).isNull();
        }
    }

    @Nested
    class BeginVisitTests {
        @Test
        void givenVisitDoesNotExists_whenBeginVisit_thenThrowEntityNotFoundException() {
            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.beginVisit(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("there is no visit for waiting list id: 3 and visitor id: 2");
        }

        @Test
        void givenVisitIsAlreadyCanceled_whenBeginVisit_thenThrowVisitAlreadyCompletedException() {
            Visit visit = new Visit(visitor, waitingList, null, null);
            visit.cancelVisit();

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));

            assertThatExceptionOfType(VisitAlreadyCanceledException.class)
                    .isThrownBy(() -> underTest.beginVisit(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("Visit has already been canceled.");
        }

        @Test
        void givenVisitIsAlreadyInProgress_whenBeginVisit_thenThrowVisitAlreadyCompletedException() {
            Visit visit = new Visit(visitor, waitingList, null, null);
            visit.beginVisit();

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));

            assertThatExceptionOfType(VisitAlreadyCompletedException.class)
                    .isThrownBy(() -> underTest.beginVisit(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("Visit has already been started.");
        }

        @Test
        void givenVisit_whenBeginVisit_thenReturnVisitResponseDto() {
            Visit visit = new Visit(visitor, waitingList, null, null);

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));
            given(mapper.toResponseDto(visit)).willAnswer(invocation -> {
                Visit v = invocation.getArgument(0);
                return new VisitResponseDto(v.getArrivalTime(), v.getStartTime(), v.getEndDate(), v.getStatus(), v.getPriority(), v.getEstimatedProcessingTime(), null, null);
            });

            VisitResponseDto actual = underTest.beginVisit(waitingList.getId(), visitor.getId());
            assertThat(actual).isNotNull();
            assertThat(actual.arrivalTime()).isEqualTo(visit.getArrivalTime());
            assertThat(actual.status()).isEqualTo(Status.IN_PROGRESS);
            assertThat(actual.startTime()).isNotNull();
            assertThat(actual.endTime()).isNull();
        }
    }

    @Nested
    class CompleteVisitTests {
        @Test
        void givenVisitDoesNotExists_whenCompleteVisit_thenThrowEntityNotFoundException() {
            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.empty());

            assertThatExceptionOfType(EntityNotFoundException.class)
                    .isThrownBy(() -> underTest.completeVisit(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("there is no visit for waiting list id: 3 and visitor id: 2");
        }

        @Test
        void givenVisitIsNotToday_whenCompleteVisit_thenThrowIllegalArgumentException() {
            Visit visit = new Visit(visitor, waitingList, null, null);
            visit.getWaitingList().setDate(LocalDate.now().minusDays(1));

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> underTest.completeVisit(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("You can't start or complete or cancel a visit that is not today");
        }

        @Test
        void givenVisitIsNotInProgress_whenCompleteVisit_thenThrowVisitAlreadyCompletedException() {
            Visit visit = new Visit(visitor, waitingList, null, null);
            visit.cancelVisit();

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));

            assertThatExceptionOfType(VisitAlreadyCompletedException.class)
                    .isThrownBy(() -> underTest.completeVisit(waitingList.getId(), visitor.getId()))
                    .withMessageContaining("Visit has already been completed.");
        }

        @Test
        void givenVisit_whenCompleteVisit_thenReturnVisitResponseDto() {
            Visit visit = new Visit(visitor, waitingList, null, null);
            visit.beginVisit();

            given(visitRepository.findById(any(VisitId.class))).willReturn(Optional.of(visit));
            given(mapper.toResponseDto(visit)).willAnswer(invocation -> {
                Visit v = invocation.getArgument(0);
                return new VisitResponseDto(v.getArrivalTime(), v.getStartTime(), v.getEndDate(), v.getStatus(), v.getPriority(), v.getEstimatedProcessingTime(), null, null);
            });

            VisitResponseDto actual = underTest.completeVisit(waitingList.getId(), visitor.getId());
            assertThat(actual).isNotNull();
            assertThat(actual.arrivalTime()).isEqualTo(visit.getArrivalTime());
            assertThat(actual.status()).isEqualTo(Status.FINISHED);
            assertThat(actual.startTime()).isNotNull();
            assertThat(actual.endTime()).isNotNull();
        }
    }
}