package com.wora.waitingroom.waitinglist.application.service.impl;

import com.wora.waitingroom.waitinglist.application.dto.response.AnalyticsResponseDto;
import com.wora.waitingroom.waitinglist.application.service.AnalyticsService;
import com.wora.waitingroom.waitinglist.domain.entity.Visit;
import com.wora.waitingroom.waitinglist.domain.repository.VisitRepository;
import com.wora.waitingroom.waitinglist.domain.vo.Status;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultAnalyticsService implements AnalyticsService {
    private final VisitRepository visitRepository;

    public AnalyticsResponseDto calculateAnalytics(WaitingListId id) {
        List<Visit> visits = visitRepository.findAllByWaitingListId(id);

        long totalVisitors = visits.size();
        long waitingCount = visits.stream().filter(v -> v.getStatus() == Status.WAITING).count();
        long inProgressCount = visits.stream().filter(v -> v.getStatus() == Status.IN_PROGRESS).count();
        long finishedCount = visits.stream().filter(v -> v.getStatus() == Status.FINISHED).count();
        long cancelledCount = visits.stream().filter(v -> v.getStatus() == Status.CANCELED).count();

        double averageWaitingTime = visits.stream()
                .filter(v -> v.getStatus() == Status.FINISHED && v.getStartTime() != null)
                .mapToDouble(v -> Duration.between(v.getArrivalTime(), v.getStartTime()).toMinutes())
                .average()
                .orElse(0.0);

        double visitorRotation = finishedCount / (double) Duration.between(
                visits.stream().map(Visit::getArrivalTime).min(LocalTime::compareTo).orElse(LocalTime.now()),
                LocalDateTime.now()
        ).toHours();

        double satisfactionRate = (double) finishedCount / totalVisitors * 100;

        return new AnalyticsResponseDto(
                averageWaitingTime,
                visitorRotation,
                satisfactionRate,
                totalVisitors,
                waitingCount,
                inProgressCount,
                finishedCount,
                cancelledCount
        );
    }
}
