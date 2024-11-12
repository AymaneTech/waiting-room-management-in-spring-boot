package com.wora.waitingRoom.visitor.application.service;

import com.wora.waitingRoom.common.domain.exception.EntityNotFoundException;
import com.wora.waitingRoom.visitor.application.service.impl.DefaultVisitorService;
import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.visitor.domain.VisitorId;
import com.wora.waitingRoom.visitor.domain.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class DefaultVisitorServiceTest {

    @Mock
    private VisitorRepository repository;
    private VisitorService sut;

    @BeforeEach
    void setup(){
        sut = new DefaultVisitorService(repository);
    }

    @Test
    void givenVisitorExists_whenFindEntityById_thenShouldReturnVisitor() {
        VisitorId visitorId = new VisitorId(2L);
        Visitor expected = new Visitor("aymane", "el maini")
                .setId(visitorId);

        given(repository.findById(visitorId)).willReturn(Optional.of(expected));

        Visitor actual = sut.findEntityById(visitorId);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
        verify(repository).findById(visitorId);
    }

    @Test
    void givenVisitorDoesNotExist_whenFindEntityById_thenShouldThrowEntityNotFoundException() {
        VisitorId visitorId = new VisitorId(2L);

        given(repository.findById(visitorId)).willReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> sut.findEntityById(visitorId))
                .withMessageContaining("visitor with id 2 not found");
        verify(repository).findById(visitorId);
    }
}