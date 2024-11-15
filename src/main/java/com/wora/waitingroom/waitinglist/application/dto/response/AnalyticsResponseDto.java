package com.wora.waitingroom.waitinglist.application.dto.response;

public record AnalyticsResponseDto(
        double averageWaitingTime,       // Average time a visitor waits (in minutes)
        double visitorRotation,          // Number of visitors served per hour
        double satisfactionRate,         // Percentage of satisfied visitors
        long totalVisitors,              // Total number of visitors
        long waitingCount,               // Visitors currently waiting
        long inProgressCount,            // Visitors currently being served
        long finishedCount,              // Visitors who completed their service
        long cancelledCount              // Visitors who cancelled their service
) {
}
