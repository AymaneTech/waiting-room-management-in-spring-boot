package com.wora.waitingroom.waitinglist.infrastructure.web;

import com.wora.waitingroom.waitinglist.application.dto.response.AnalyticsResponseDto;
import com.wora.waitingroom.waitinglist.application.service.AnalyticsService;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/waiting-lists")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/{id}/analytics")
    public ResponseEntity<AnalyticsResponseDto> getAnalytics(@PathVariable Long id) {
        AnalyticsResponseDto response = analyticsService.calculateAnalytics(new WaitingListId(id));
        return ResponseEntity.ok(response);
    }
}
