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
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class AnalyticsControllerIntegrationTest {
    private static final String REQUEST_MAPPING = "/api/v1/waiting-lists/";

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
    void givenWaitingList_whenGetAnalytics_shouldReturnAnalyticsData() throws Exception {
        mockMvc.perform(get(REQUEST_MAPPING + waitingListId + "/analytics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageWaitingTime").exists())
                .andExpect(jsonPath("$.visitorRotation").exists())
                .andExpect(jsonPath("$.satisfactionRate").exists())
                .andExpect(jsonPath("$.totalVisitors").exists());
    }

}