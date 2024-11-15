package com.wora.waitingRoom.waitinglist.domain.service;

import com.wora.waitingroom.visitor.domain.Visitor;
import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.exception.MultipleWaitingListsFoundException;
import com.wora.waitingroom.waitinglist.domain.service.Scheduler;
import com.wora.waitingroom.waitinglist.domain.service.impl.HpfScheduler;
import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@ExtendWith(SpringExtension.class)
class HpfSchedulerTest {
    private Scheduler underTest;
    private List<Visit> visits;
    private List<Visitor> visitors;
    private WaitingList waitingList;

    @BeforeEach
    void setup() {
        this.underTest = new HpfScheduler();
        waitingList = WaitingList.builder()
                .id(new WaitingListId(1L))
                .date(LocalDate.now())
                .algorithm(Algorithm.HPF)
                .capacity(5)
                .build();

        visitors = List.of(
                new Visitor("yahya", "el maini"),
                new Visitor("abdelhak", "azrour"),
                new Visitor("hamza", "lamin"),
                new Visitor("soufiane", "bouanani"));
    }

    @Test
    void givenVisitsDontBelongToSameWaitingList_whenSchedule_thenThrowMultipleWaitingListsFoundException() {
        WaitingList otherWaitingList = WaitingList.builder()
                .id(new WaitingListId(7L))
                .date(LocalDate.now())
                .algorithm(Algorithm.FIFO)
                .capacity(8)
                .build();

        Visitor otherVisitor = new Visitor("abdelhak", "azrour");
        visits = List.of(new Visit(visitors.getFirst(), waitingList, null, null),
                new Visit(otherVisitor, otherWaitingList, null, null)
        );

        assertThatExceptionOfType(MultipleWaitingListsFoundException.class)
                .isThrownBy(() -> underTest.schedule(visits))
                .withMessage("Visits are not for the same waiting list");
    }

    @Test
    void givenEmptyList_whenSchedule_thenReturnEmptyList() {
        List<Visit> actual = underTest.schedule(List.of());

        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void givenValidVisitsLists_whenSchedule_thenReturnOrderedVisitsByArrivalTime() {
        visits = List.of(
                new Visit(visitors.get(0), waitingList, (byte) 50, null),
                new Visit(visitors.get(1), waitingList, (byte) 120, null),
                new Visit(visitors.get(2), waitingList, (byte) 100, null),
                new Visit(visitors.get(3), waitingList, (byte) 70, null)
        );
        List<Visit> actual = underTest.schedule(visits);

        assertThat(actual.get(0).getPriority()).isEqualTo((byte) 50);
        assertThat(actual.get(1).getPriority()).isEqualTo((byte) 70);
        assertThat(actual.getLast().getPriority()).isEqualTo((byte) 120);
    }

    @Test
    void givenVisitsWithSamePriority_whenSchedule_thenReturnVisitOrderedByArrivalTime() {
        visits = List.of(
                new Visit(visitors.get(0), waitingList, null, null),
                new Visit(visitors.get(1), waitingList, null, null),
                new Visit(visitors.get(2), waitingList, null, null),
                new Visit(visitors.get(3), waitingList, null, null)
        );
        LocalTime firstOne = LocalTime.now().minusMinutes(4);
        LocalTime secondOne = LocalTime.now().plusMinutes(11);
        LocalTime lastOne = LocalTime.now().plusMinutes(40);

        visits.get(0).setArrivalTime(firstOne);
        visits.get(1).setArrivalTime(secondOne);
        visits.get(2).setArrivalTime(lastOne);
        visits.get(3).setArrivalTime(LocalTime.now().plusMinutes(30));

        List<Visit> actual = underTest.schedule(visits);

        assertThat(actual.get(0).getArrivalTime()).isEqualTo(firstOne);
        assertThat(actual.get(1).getArrivalTime()).isEqualTo(secondOne);
        assertThat(actual.getLast().getArrivalTime()).isEqualTo(lastOne);
        assertThat(actual.get(0).getPriority()).isEqualTo((byte) 255);
    }

}