package com.wora.waitingRoom.waitingList.domain.service;

import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.waitingList.domain.entity.Visit;
import com.wora.waitingRoom.waitingList.domain.entity.WaitingList;
import com.wora.waitingRoom.waitingList.domain.service.impl.HpfScheduler;
import com.wora.waitingRoom.waitingList.domain.service.impl.SpfScheduler;
import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class SpfSchedulerTest {
    private Scheduler underTest;
    private List<Visit> visits;
    private List<Visitor> visitors;
    private WaitingList waitingList;

    @BeforeEach
    void setup() {
        this.underTest = new SpfScheduler();
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
    void givenValidVisitsLists_whenSchedule_thenReturnOrderedVisitsByArrivalTime() {
        visits = List.of(
                new Visit(visitors.get(0), waitingList, null, Duration.ofHours(2)),
                new Visit(visitors.get(1), waitingList, null, Duration.ofHours(1)),
                new Visit(visitors.get(3), waitingList, null,Duration.ofMinutes(30)),
                new Visit(visitors.get(2), waitingList, null, Duration.ofHours(3))
        );
        List<Visit> actual = underTest.schedule(visits);

        assertThat(actual.get(0).getEstimatedProcessingTime()).isEqualTo(Duration.ofMinutes(30));
        assertThat(actual.get(1).getEstimatedProcessingTime()).isEqualTo(Duration.ofHours(1));
        assertThat(actual.getLast().getEstimatedProcessingTime()).isEqualTo(Duration.ofHours(3));
    }

}