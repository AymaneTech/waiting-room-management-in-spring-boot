package com.wora.waitingRoom.waitinglist.infrastructure.web;

import com.wora.waitingroom.Application;
import com.wora.waitingroom.visitor.domain.Visitor;
import com.wora.waitingroom.visitor.domain.VisitorRepository;
import com.wora.waitingroom.waitinglist.application.dto.request.VisitRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.request.WaitingListRequestDto;
import com.wora.waitingroom.waitinglist.application.service.VisitService;
import com.wora.waitingroom.waitinglist.application.service.WaitingListService;
import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.Mode;
import com.wora.waitingroom.waitinglist.domain.vo.Status;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class SchedulingControllerIntegrationTest {
    private static final String REQUEST_MAPPING = "/api/v1/visits";

    private final MockMvc mockMvc;
    private final WaitingListService waitingListService;
    private final VisitorRepository visitorRepository;
    private final VisitService visitService;

    private Long waitingListId;

    @BeforeEach
    void setup() {
        List<Visitor> visitors = visitorRepository.findAll();
        waitingListId = waitingListService.create(
                new WaitingListRequestDto(LocalDate.now(), 10, Mode.PART_TIME, Algorithm.FIFO)
        ).id();

        for (int i = 0; i < visitors.size(); i++) {
            visitService.subscribeVisitor(
                    new WaitingListId(waitingListId),
                    visitors.get(i).getId(),
                    new VisitRequestDto((byte) (i + 1), null)
            );
        }
    }

    @Test
    @Rollback
    void givenValidWaitingListId_whenScheduleVisits_thenReturnScheduledVisitsOrderedByPriority() throws Exception {
        mockMvc.perform(get(REQUEST_MAPPING + "/{waitingListId}/schedule", waitingListId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalElements", is(5)))
                .andExpect(jsonPath("$.content[*].status", everyItem(is("WAITING"))));
    }

    @Test
    @Rollback
    void givenInvalidWaitingListId_whenScheduleVisits_thenReturnNotFound() throws Exception {
        Long invalidWaitingListId = 999L;

        mockMvc.perform(get(REQUEST_MAPPING + "/{waitingListId}/schedule", invalidWaitingListId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", containsString("waiting list with id 999 not found")));
    }

    @Test
    @Rollback
    void givenValidWaitingListIdAndStatus_whenFindVisitsByStatus_thenReturnFilteredVisits() throws Exception {
        Status status = Status.WAITING;

        mockMvc.perform(get(REQUEST_MAPPING + "/{waitingListId}", waitingListId)
                        .param("status", status.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalElements", is(5)))
                .andExpect(jsonPath("$.content[*].status", everyItem(is("WAITING"))));
    }

    @Test
    @Rollback
    void givenInvalidWaitingListIdAndStatus_whenFindVisitsByStatus_thenReturnNotFound() throws Exception {
        Long invalidWaitingListId = 999L;
        Status status = Status.WAITING;

        mockMvc.perform(get(REQUEST_MAPPING + "/{waitingListId}", invalidWaitingListId)
                        .param("status", status.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors", containsString("waiting list with id 999 not found")));
    }

    @Test
    @Rollback
    void givenEmptyWaitingList_whenScheduleVisits_thenReturnEmptyPage() throws Exception {
        Long emptyWaitingListId = waitingListService.create(
                new WaitingListRequestDto(LocalDate.now(), 10, Mode.PART_TIME, Algorithm.FIFO)
        ).id();

        mockMvc.perform(get(REQUEST_MAPPING + "/{waitingListId}/schedule", emptyWaitingListId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)))
                .andExpect(jsonPath("$.totalElements", is(0)));
    }
}