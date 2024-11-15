package com.wora.waitingRoom.waitingList.domain.service;

import com.wora.waitingroom.visitor.domain.Visitor;
import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.exception.MultipleWaitingListsFoundException;
import com.wora.waitingroom.waitinglist.domain.service.Scheduler;
import com.wora.waitingroom.waitinglist.domain.service.impl.FifoScheduler;
import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
class FifoSchedulerTest {
    private Scheduler underTest;
    private List<Visit> visits;
    private WaitingList waitingList;

    private LocalTime arrivalTime1;
    private LocalTime arrivalTime2;
    private LocalTime arrivalTime3;

    @BeforeEach
    void setup() {
        this.underTest = new FifoScheduler();
        waitingList = WaitingList.builder()
                .id(new WaitingListId(1L))
                .date(LocalDate.now())
                .algorithm(Algorithm.FIFO)
                .capacity(5)
                .build();

        List<Visitor> visitors = List.of(
                new Visitor("yahya", "el maini"),
                new Visitor("abdelhak", "azrour"),
                new Visitor("hamza", "lamin"),
                new Visitor("soufiane", "bouanani"));

        visits = visitors.stream()
                .map(v -> new Visit(v, waitingList, null, null))
                .toList();

        arrivalTime1 = LocalTime.now().plusMinutes(2);
        arrivalTime2 = LocalTime.now().plusMinutes(10);
        arrivalTime3 = LocalTime.now().plusMinutes(90);

        visits.get(2).setArrivalTime(arrivalTime1);
        visits.get(1).setArrivalTime(arrivalTime2);
        visits.get(0).setArrivalTime(arrivalTime3);
        visits.get(3).setArrivalTime(LocalTime.now().plusMinutes(50));
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
        List<Visit> visits1 = new ArrayList<>(visits);
        visits1.add(new Visit(otherVisitor, otherWaitingList, null, null));

        assertThatExceptionOfType(MultipleWaitingListsFoundException.class)
                .isThrownBy(() -> underTest.schedule(visits1))
                .withMessage("Visits are not for the same waiting list");
    }

    @Test
    void givenValidVisitsLists_whenSchedule_thenReturnOrderedVisitsByArrivalTime() {
        List<Visit> actual = underTest.schedule(visits);

        assertThat(actual.get(0).getArrivalTime()).isEqualTo(arrivalTime1);
        assertThat(actual.get(1).getArrivalTime()).isEqualTo(arrivalTime2);
        assertThat(actual.getLast().getArrivalTime()).isEqualTo(arrivalTime3);
    }
}