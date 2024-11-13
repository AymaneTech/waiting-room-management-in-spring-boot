package com.wora.waitingRoom.waitingList.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.visitor.domain.VisitorId;
import com.wora.waitingRoom.visitor.domain.VisitorRepository;
import com.wora.waitingRoom.waitingList.application.dto.request.VisitRequestDto;
import com.wora.waitingRoom.waitingList.application.dto.request.WaitingListRequestDto;
import com.wora.waitingRoom.waitingList.application.service.VisitService;
import com.wora.waitingRoom.waitingList.application.service.WaitingListService;
import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import com.wora.waitingRoom.waitingList.domain.valueObject.WaitingListId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.wora.waitingRoom.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class VisitControllerIntegrationTest {
    private final static String REQUEST_MAPPING = "/api/v1/visits";

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final WaitingListService waitingListService;
    private final VisitorRepository visitorRepository;
    private final VisitService visitService;

    private Long visitorId;
    private Long waitingListId;

    @BeforeEach
    void setup() {
        visitorId = visitorRepository.save(new Visitor("John", "Doe")).getId().value();
        waitingListId = waitingListService.create(new WaitingListRequestDto(LocalDate.now(), 10, Mode.PART_TIME, Algorithm.FIFO)).id();
    }

    @Nested
    class SubscribeVisitorTests {
        @Test
        @Rollback
        void givenWaitingListAndVisitorExists_whenSubscribeVisitor_thenReturnsVisit() throws Exception {
            mockMvc.perform(post(REQUEST_MAPPING + "/subscribe/" + waitingListId + "/1")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content("{}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.arrivalTime").isNotEmpty())
                    .andExpect(jsonPath("$.startTime").isEmpty())
                    .andExpect(jsonPath("$.status").value("WAITING"));
        }

        @Test
        @Rollback
        void givenWaitingListDoesNotExist_whenSubscribeVisitor_thenReturnsNotFound() throws Exception {
            mockMvc.perform(post(REQUEST_MAPPING + "/subscribe/999/1")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content("{}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenVisitorDoesNotExist_whenSubscribeVisitor_thenReturnsNotFound() throws Exception {
            mockMvc.perform(post(REQUEST_MAPPING + "/subscribe/" + waitingListId + "/999")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content("{}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }
    }

    @Nested
    @Rollback
    class CancelSubscriptionTests {
        @Test
        void givenVisitExists_whenCancelSubscription_thenReturnsVisit() throws Exception {
            visitService.subscribeVisitor(new WaitingListId(waitingListId), new VisitorId(visitorId), new VisitRequestDto(null, null));
            mockMvc.perform(post(REQUEST_MAPPING + "/cancel/" + waitingListId + "/" + visitorId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.arrivalTime").isNotEmpty())
                    .andExpect(jsonPath("$.startTime").isEmpty())
                    .andExpect(jsonPath("$.status").value("CANCELED"));
        }

        @Test
        void givenVisitDoesNotExist_whenCancelSubscription_thenReturnsNotFound() throws Exception {
            mockMvc.perform(post(REQUEST_MAPPING + "/cancel/" + waitingListId + "/999")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        void givenVisitorDoesntSubscribe_whenCancelSubscription_thenReturnsBadRequest() throws Exception {
            mockMvc.perform(post(REQUEST_MAPPING + "/cancel/" + waitingListId + "/" + visitorId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("there is no visit for waiting list id: " + waitingListId + " and visitor id: " + visitorId));
        }
    }

    @Nested
    class BeginVisitTests {
        @Test
        void givenVisitExists_whenBeginVisit_thenReturnsVisit() throws Exception {
            visitService.subscribeVisitor(new WaitingListId(waitingListId), new VisitorId(visitorId), new VisitRequestDto(null, null));
            mockMvc.perform(post(REQUEST_MAPPING + "/begin/" + waitingListId + "/" + visitorId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.arrivalTime").isNotEmpty())
                    .andExpect(jsonPath("$.startTime").isNotEmpty())
                    .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
        }

        @Test
        void givenVisitDoesNotExist_whenBeginVisit_thenReturnsNotFound() throws Exception {
            mockMvc.perform(post(REQUEST_MAPPING + "/begin/" + waitingListId + "/999")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        void givenVisitorDoesntSubscribe_whenBeginVisit_thenReturnsBadRequest() throws Exception {
            mockMvc.perform(post(REQUEST_MAPPING + "/begin/" + waitingListId + "/" + visitorId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("there is no visit for waiting list id: " + waitingListId + " and visitor id: " + visitorId));
        }
    }

    @Nested
    @Rollback
    class CompleteVisitTests {
        @Test
        void givenVisitExists_whenCompleteVisit_thenReturnsVisit() throws Exception {
            visitService.subscribeVisitor(new WaitingListId(waitingListId), new VisitorId(visitorId), new VisitRequestDto(null, null));
            visitService.beginVisit(new WaitingListId(waitingListId), new VisitorId(visitorId));
            mockMvc.perform(post(REQUEST_MAPPING + "/complete/" + waitingListId + "/" + visitorId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.arrivalTime").isNotEmpty())
                    .andExpect(jsonPath("$.startTime").isNotEmpty())
                    .andExpect(jsonPath("$.status").value("FINISHED"));
        }

        @Test
        void givenVisitorDoesntBeginVisit_whenCompleteVisit_thenReturnsBadRequest() throws Exception {
            visitService.subscribeVisitor(new WaitingListId(waitingListId), new VisitorId(visitorId), new VisitRequestDto(null, null));
            mockMvc.perform(post(REQUEST_MAPPING + "/complete/" + waitingListId + "/" + visitorId)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.errors").value("Visit has already been completed."));
        }

        @Test
        void givenVisitDoesNotExist_whenCompleteVisit_thenReturnsNotFound() throws Exception {
            mockMvc.perform(post(REQUEST_MAPPING + "/complete/" + waitingListId + "/999")
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }
    }
}