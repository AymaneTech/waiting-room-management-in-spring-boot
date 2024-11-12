package com.wora.waitingRoom.visitor.infrastructure;

import com.wora.waitingRoom.visitor.domain.VisitorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class VisitorSeederTest {
    @Mock
    private VisitorRepository repository;
    @InjectMocks
    private VisitorSeeder seeder;

    @Test
    void givenVisitorsDoesNotExists_whenCommandLineRunnerRuns_shouldSaveVisitors() throws Exception {
        given(repository.count()).willReturn(5L);

        seeder.createVisitors()
                .run();

        verify(repository, never()).saveAll(anyList());
    }

    @Test
    void givenVisitorsExists_whenCommandLineRunnerRuns_shouldNotSaveVisitors() throws Exception {
        given(repository.count()).willReturn(0L);

        seeder.createVisitors()
                .run();

        verify(repository).saveAll(anyList());
    }
}