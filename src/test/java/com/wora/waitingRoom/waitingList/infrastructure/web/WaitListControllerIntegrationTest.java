package com.wora.waitingRoom.waitingList.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wora.waitingroom.Application;
import com.wora.waitingroom.config.configurationProperties.WaitingListConfigurationProperties;
import com.wora.waitingroom.waitinglist.application.dto.request.WaitingListRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.WaitingListResponseDto;
import com.wora.waitingroom.waitinglist.application.service.WaitingListService;
import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.Mode;
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

import static com.wora.waitingroom.common.infrastructure.web.GlobalExceptionHandler.ENTITY_NOT_FOUND_MESSAGE;
import static com.wora.waitingroom.common.infrastructure.web.GlobalExceptionHandler.VALIDATION_FAILED_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class WaitListControllerIntegrationTest {
    private static final String REQUEST_MAPPING = "/api/v1/waiting-lists";

    private final MockMvc mockMvc;
    private final WaitingListService service;
    private final ObjectMapper objectMapper;
    private final WaitingListConfigurationProperties configurationProperties;
    private WaitingListResponseDto waitingList;

    @BeforeEach
    void setup() {
        waitingList = service.create(new WaitingListRequestDto(LocalDate.now(), 10, Mode.PART_TIME, Algorithm.FIFO));
    }


    @Test
    @Rollback
    void givenWaitingListsExists_whenFindAll_thenReturnsAllWaitingLists() throws Exception {
        mockMvc.perform(get(REQUEST_MAPPING))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isNotEmpty())
                .andExpect(jsonPath("$.content[0].id").value(waitingList.id()))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Nested
    class FindByIdTests {
        @Test
        @Rollback
        void givenWaitingListDoesNotExists_whenFindById_thenReturnsNotFound() throws Exception {
            mockMvc.perform(get(REQUEST_MAPPING + "/{id}", 0))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenWaitingListsExists_whenFindById_thenReturnsWaitingList() throws Exception {
            mockMvc.perform(get(REQUEST_MAPPING + "/{id}", waitingList.id()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(waitingList.id()));
        }
    }

    @Nested
    class CreateTests {
        @Test
        @Rollback
        void givenInvalidRequest_whenCreate_thenReturnsValidationError() throws Exception {
            WaitingListRequestDto requestDto = new WaitingListRequestDto(LocalDate.now(), -10, Mode.PART_TIME, Algorithm.FIFO);
            mockMvc.perform(post(REQUEST_MAPPING)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.errors.capacity").value("must be greater than 0"));
        }

        @Test
        @Rollback
        void givenValidRequest_whenCreate_thenReturnsCreateWaitingList() throws Exception {
            WaitingListRequestDto requestDto = new WaitingListRequestDto(LocalDate.now(), 10, Mode.PART_TIME, Algorithm.FIFO);
            mockMvc.perform(post(REQUEST_MAPPING)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        @Rollback
        void givenValidRequestWithDefaultValues_whenCreate_thenReturnsCreateWaitingList() throws Exception {
            WaitingListRequestDto requestDto = new WaitingListRequestDto(LocalDate.now(), 10, null, null);
            mockMvc.perform(post(REQUEST_MAPPING)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.mode").value(configurationProperties.mode().name()))
                    .andExpect(jsonPath("$.algorithm").value(configurationProperties.algorithm().name()));
        }
    }

    @Nested
    class UpdateTests {
        @Test
        @Rollback
        void givenInvalidRequest_whenUpdate_thenReturnsValidationError() throws Exception {
            WaitingListRequestDto requestDto = new WaitingListRequestDto(LocalDate.now(), -10, Mode.PART_TIME, Algorithm.FIFO);
            mockMvc.perform(put(REQUEST_MAPPING + "/{id}", waitingList.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(VALIDATION_FAILED_MESSAGE))
                    .andExpect(jsonPath("$.errors.capacity").value("must be greater than 0"));
        }

        @Test
        @Rollback
        void givenNotExistentId_whenUpdate_thenReturnsEntityNotFound() throws Exception {
            WaitingListRequestDto requestDto = new WaitingListRequestDto(LocalDate.now(), 10, Mode.PART_TIME, Algorithm.FIFO);
            mockMvc.perform(put(REQUEST_MAPPING + "/{id}", 393939L)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenValidRequest_whenUpdate_thenReturnsUpdatedWaitingList() throws Exception {
            WaitingListRequestDto requestDto = new WaitingListRequestDto(LocalDate.now(), 10, Mode.PART_TIME, Algorithm.FIFO);
            mockMvc.perform(put(REQUEST_MAPPING + "/{id}", waitingList.id())
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(waitingList.id()))
                    .andExpect(jsonPath("$.date").value(requestDto.date().toString()))
                    .andExpect(jsonPath("$.capacity").value(requestDto.capacity()))
                    .andExpect(jsonPath("$.mode").value(requestDto.mode().name()))
                    .andExpect(jsonPath("$.algorithm").value(requestDto.algorithm().name()));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        @Rollback
        void givenNotExistentId_whenDelete_thenReturnsEntityNotFound() throws Exception {
            mockMvc.perform(delete(REQUEST_MAPPING + "/{id}", 393939L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(ENTITY_NOT_FOUND_MESSAGE));
        }

        @Test
        @Rollback
        void givenExistentId_whenDelete_thenReturnsNoContent() throws Exception {
            mockMvc.perform(delete(REQUEST_MAPPING + "/{id}", waitingList.id()))
                    .andExpect(status().isNoContent());
        }
    }
}
