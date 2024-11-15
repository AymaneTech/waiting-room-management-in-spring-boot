package com.wora.waitingroom.waitinglist.infrastructure.web;

import com.wora.waitingroom.waitinglist.application.dto.response.VisitResponseDto;
import com.wora.waitingroom.waitinglist.application.service.VisitsSchedulingService;
import com.wora.waitingroom.waitinglist.domain.vo.Status;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class SchedulingController {
    private final VisitsSchedulingService schedulingService;

    @GetMapping("/{waitingListId}/schedule")
    public ResponseEntity<Page<VisitResponseDto>> scheduleVisits(@PathVariable Long waitingListId) {
        Page<VisitResponseDto> visitResponseDtos = schedulingService.scheduleVisits(new WaitingListId(waitingListId));
        return ResponseEntity.ok(visitResponseDtos);
    }

    @GetMapping("/{waitingListId}")
    public ResponseEntity<Page<VisitResponseDto>> findVisitsOfWaitingListByStatus(@PathVariable Long waitingListId, @RequestParam(defaultValue = "WAITING") Status status) {
        Page<VisitResponseDto> visitResponseDtos = schedulingService.scheduleVisitsByStatus(new WaitingListId(waitingListId), status);
        return ResponseEntity.ok(visitResponseDtos);
    }
}
