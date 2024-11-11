package com.wora.waitingRoom.visitor.infrastructure;

import java.util.List;

import com.wora.waitingRoom.visitor.domain.Visitor;
import com.wora.waitingRoom.visitor.domain.VisitorRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VisitorSeeder {

    private final VisitorRepository visitorRepository;

    @Bean
    public CommandLineRunner createVisitors() {
        return args -> {
            System.out.println("-- visitor seeder --");
            if (visitorRepository.count() != 0)
                return;

            System.out.println("-- inserting visitors --");
            visitorRepository.saveAll(List.of(
                    new Visitor("aymane", "elmaini"),
                    new Visitor("yahya", "el maini"),
                    new Visitor("abdelhak", "azrour"),
                    new Visitor("hamza", "lamin"),
                    new Visitor("soufiane", "bouanani")));
        };

    }

}
