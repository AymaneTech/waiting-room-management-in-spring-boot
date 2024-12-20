package com.wora.waitingroom.waitinglist.infrastructure.web;

import com.wora.waitingroom.visitor.domain.VisitorId;
import com.wora.waitingroom.waitinglist.application.dto.request.VisitRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.VisitResponseDto;
import com.wora.waitingroom.waitinglist.application.service.VisitService;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService service;

    @PostMapping("subscribe/{waitingListId}/{visitorId}")
    public ResponseEntity<VisitResponseDto> subscribeVisitor(@PathVariable Long waitingListId,
                                                             @PathVariable Long visitorId,
                                                             @RequestBody @Valid VisitRequestDto request) {
        VisitResponseDto visit = service.subscribeVisitor(new WaitingListId(waitingListId), new VisitorId(visitorId), request);
        return new ResponseEntity<>(visit, HttpStatus.CREATED);
    }

    @PostMapping("cancel/{waitingListId}/{visitorId}")
    public ResponseEntity<VisitResponseDto> cancelSubscription(@PathVariable Long waitingListId,
                                                              @PathVariable Long visitorId) {
        VisitResponseDto visit = service.cancelSubscription(new WaitingListId(waitingListId), new VisitorId(visitorId));
        return new ResponseEntity<>(visit, HttpStatus.OK);
    }

    @PostMapping("begin/{waitingListId}/{visitorId}")
    public ResponseEntity<VisitResponseDto> beginVisit(@PathVariable Long waitingListId,
                                                      @PathVariable Long visitorId) {
        VisitResponseDto visit = service.beginVisit(new WaitingListId(waitingListId), new VisitorId(visitorId));
        return new ResponseEntity<>(visit, HttpStatus.OK);
    }

    @PostMapping("complete/{waitingListId}/{visitorId}")
    public ResponseEntity<VisitResponseDto> completeVisit(@PathVariable Long waitingListId,
                                                         @PathVariable Long visitorId) {
        VisitResponseDto visit = service.completeVisit(new WaitingListId(waitingListId), new VisitorId(visitorId));
        return new ResponseEntity<>(visit, HttpStatus.OK);
    }
}
