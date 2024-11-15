package com.wora.waitingRoom.waitingList.application.service;

import com.wora.waitingroom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingroom.config.configurationProperties.WaitingListConfigurationProperties;
import com.wora.waitingroom.waitinglist.application.dto.request.WaitingListRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.WaitingListResponseDto;
import com.wora.waitingroom.waitinglist.application.mapper.WaitingListMapper;
import com.wora.waitingroom.waitinglist.application.service.WaitingListService;
import com.wora.waitingroom.waitinglist.application.service.impl.DefaultWaitingListService;
import com.wora.waitingroom.waitinglist.domain.entity.WaitingList;
import com.wora.waitingroom.waitinglist.domain.repository.WaitingListRepository;
import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.Mode;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class DefaultWaitingListServiceTest {
    @Mock
    private WaitingListRepository repository;
    @Mock
    private WaitingListMapper mapper;
    private WaitingListConfigurationProperties configurationProperties;
    private WaitingListService sut;

    private WaitingList waitingList1;
    private WaitingList waitingList2;

    @BeforeEach
    void setUp() {
        configurationProperties = new WaitingListConfigurationProperties(30, Mode.FULL_TIME, Algorithm.FIFO);
        sut = new DefaultWaitingListService(repository, mapper, configurationProperties);
        waitingList1 = WaitingList.builder()
                .id(new WaitingListId(1L))
                .date(LocalDate.now())
                .capacity(30)
                .mode(Mode.FULL_TIME)
                .algorithm(Algorithm.FIFO)
                .build();
        waitingList2 = WaitingList.builder()
                .id(new WaitingListId(2L))
                .date(LocalDate.now().plusDays(1))
                .mode(Mode.PART_TIME)
                .algorithm(Algorithm.HPF)
                .build();
    }

    @Nested
    class FindAllTests {
        @Test
        void givenWaitingListsDoesNotExists_whenFindAll_thenReturnEmptyPage() {
            given(repository.findAll(PageRequest.of(0, 10)))
                    .willReturn(Page.empty());

            Page<WaitingListResponseDto> actual = sut.findAll(0, 10);

            assertThat(actual.getTotalElements()).isZero();
            assertThat(actual.getContent()).isEmpty();
        }

        @Test
        void givenWaitingLists_whenFindALl_thenReturnPageOfWaitingListResponseDto() {
            given(repository.findAll(PageRequest.of(0, 10)))
                    .willReturn(new PageImpl<>(List.of(waitingList1, waitingList2)));

            given(mapper.toResponseDto(any(WaitingList.class)))
                    .willAnswer(invocation -> {
                        WaitingList waitingList = invocation.getArgument(0);
                        return new WaitingListResponseDto(waitingList.getId().value(),
                                waitingList.getDate(),
                                waitingList.getCapacity(),
                                waitingList.getMode(),
                                waitingList.getAlgorithm());
                    });

            Page<WaitingListResponseDto> actual = sut.findAll(0, 10);

            assertThat(actual.getTotalElements()).isEqualTo(2);
            assertThat(actual.getContent()).hasSize(2);
        }
    }

    @Nested
    class FindById {
        @Test
        void givenWaitingListDoesNotExists_whenFindById_thenThrowEntityNotFoundException() {
            given(repository.findById(new WaitingListId(1L)))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.findById(new WaitingListId(1L)))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("waiting list with id 1 not found");
        }

        @Test
        void givenWaitingListExists_whenFindById_thenReturnWaitingListResponseDto() {
            given(repository.findById(new WaitingListId(1L)))
                    .willReturn(Optional.of(waitingList1));

            given(mapper.toResponseDto(waitingList1))
                    .willReturn(new WaitingListResponseDto(waitingList1.getId().value(), waitingList1.getDate(), waitingList1.getCapacity(), waitingList1.getMode(), waitingList1.getAlgorithm()));

            WaitingListResponseDto actual = sut.findById(waitingList1.getId());

            assertThat(actual.id()).isEqualTo(waitingList1.getId().value());
            assertThat(actual.date()).isEqualTo(waitingList1.getDate());
        }
    }

    @Nested
    class CreateTests {
        private static Stream<Arguments> waitingListRequestProvider() {
            LocalDate date = LocalDate.now();
            return Stream.of(
                    Arguments.of(
                            new WaitingListRequestDto(date, 20, Mode.PART_TIME, Algorithm.HPF),
                            Mode.PART_TIME,
                            Algorithm.HPF
                    ),
                    Arguments.of(
                            new WaitingListRequestDto(date, 20, null, null),
                            Mode.FULL_TIME,
                            Algorithm.FIFO
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("waitingListRequestProvider")
        @DisplayName("Given waiting list request dto, when create, then return waiting list response dto")
        void givenWaitingListRequestDto_whenCreate_thenReturnWaitingListResponseDto(
                WaitingListRequestDto requestDto,
                Mode expectedMode,
                Algorithm expectedAlgorithm) {

            WaitingList waitingList = WaitingList.builder()
                    .id(new WaitingListId(1L))
                    .date(requestDto.date())
                    .capacity(requestDto.capacity())
                    .mode(expectedMode)
                    .algorithm(expectedAlgorithm)
                    .build();

            WaitingListResponseDto expectedResponse = new WaitingListResponseDto(
                    waitingList.getId().value(),
                    waitingList.getDate(),
                    waitingList.getCapacity(),
                    waitingList.getMode(),
                    waitingList.getAlgorithm()
            );

            given(repository.save(any(WaitingList.class))).willReturn(waitingList);
            given(mapper.toResponseDto(any(WaitingList.class))).willReturn(expectedResponse);

            WaitingListResponseDto actual = sut.create(requestDto);

            assertThat(actual).isNotNull();
            assertThat(actual.id()).isEqualTo(waitingList.getId().value());
            assertThat(actual.date()).isEqualTo(waitingList.getDate());
            assertThat(actual.capacity()).isEqualTo(requestDto.capacity());
            assertThat(actual.mode()).isEqualTo(expectedMode);
            assertThat(actual.algorithm()).isEqualTo(expectedAlgorithm);
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void givenWaitingListDoesNotExists_whenUpdate_thenThrowEntityNotFoundException() {
            given(repository.findById(new WaitingListId(1L)))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.update(new WaitingListId(1L), new WaitingListRequestDto(LocalDate.now(), 20, Mode.PART_TIME, Algorithm.HPF)))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("waiting list with id 1 not found");
        }

        @Test
        void givenWaitingListExists_whenUpdate_thenReturnWaitingListResponseDto() {
            WaitingListRequestDto requestDto = new WaitingListRequestDto(LocalDate.now(), 20, Mode.PART_TIME, Algorithm.HPF);
            WaitingList waitingList = WaitingList.builder()
                    .id(new WaitingListId(1L))
                    .date(requestDto.date())
                    .capacity(requestDto.capacity())
                    .mode(requestDto.mode())
                    .algorithm(requestDto.algorithm())
                    .build();

            given(repository.findById(new WaitingListId(1L)))
                    .willReturn(Optional.of(waitingList));

            given(repository.save(waitingList))
                    .willReturn(waitingList);

            given(mapper.toResponseDto(waitingList))
                    .willReturn(new WaitingListResponseDto(waitingList.getId().value(), waitingList.getDate(), waitingList.getCapacity(), waitingList.getMode(), waitingList.getAlgorithm()));

            WaitingListResponseDto actual = sut.update(new WaitingListId(1L), requestDto);

            assertThat(actual.id()).isEqualTo(waitingList.getId().value());
            assertThat(actual.date()).isEqualTo(waitingList.getDate());
            assertThat(actual.capacity()).isEqualTo(waitingList.getCapacity());
            assertThat(actual.mode()).isEqualTo(waitingList.getMode());
            assertThat(actual.algorithm()).isEqualTo(waitingList.getAlgorithm());
        }
    }

    @Nested
    class DeleteTests {
        private final WaitingListId waitingListId = new WaitingListId(1L);

        @Test
        void givenWaitingListDoesNotExists_whenDelete_thenThrowEntityNotFoundException() {
            given(repository.existsById(waitingListId))
                    .willReturn(false);

            assertThatThrownBy(() -> sut.delete(waitingListId))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("waiting list with id 1 not found");
        }

        @Test
        void givenWaitingListExists_whenDelete_thenDeleteWaitingList() {
            given(repository.existsById(waitingListId))
                    .willReturn(true);

            sut.delete(waitingListId);

            verify(repository).deleteById(waitingListId);
        }
    }
}