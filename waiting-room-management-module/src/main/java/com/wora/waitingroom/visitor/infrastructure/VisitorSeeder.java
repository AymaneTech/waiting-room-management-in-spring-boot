package com.wora.waitingroom.visitor.infrastructure;

import com.wora.waitingroom.visitor.domain.Visitor;
import com.wora.waitingroom.visitor.domain.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class VisitorSeeder {

    private final VisitorRepository visitorRepository;

    @Bean
    public CommandLineRunner createVisitors() {
        return args -> {
            log.debug("-- visitor seeder --");
            if (visitorRepository.count() != 0)
                return;

            log.debug("-- inserting visitors --");
            visitorRepository.saveAll(List.of(
                    new Visitor("aymane", "elmaini"),
                    new Visitor("yahya", "el maini"),
                    new Visitor("abdelhak", "azrour"),
                    new Visitor("hamza", "lamin"),
                    new Visitor("soufiane", "bouanani")));
        };

    }

}
