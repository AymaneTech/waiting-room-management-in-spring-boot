package com.wora.waitingroom.waitinglist.infrastructure.web;

import com.wora.waitingroom.waitinglist.application.dto.request.WaitingListRequestDto;
import com.wora.waitingroom.waitinglist.application.dto.response.WaitingListResponseDto;
import com.wora.waitingroom.waitinglist.application.service.WaitingListService;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/waiting-lists")
@RequiredArgsConstructor
public class WaitListController {
    private final WaitingListService service;

    @GetMapping
    public ResponseEntity<Page<WaitingListResponseDto>> findAll(
            @RequestParam(defaultValue = "0") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<WaitingListResponseDto> waitingLists = service.findAll(pageNum, pageSize);
        return ResponseEntity.ok(waitingLists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WaitingListResponseDto> findById(@PathVariable Long id) {
        WaitingListResponseDto waitingList = service.findById(new WaitingListId(id));
        return ResponseEntity.ok(waitingList);
    }

    @PostMapping
    public ResponseEntity<WaitingListResponseDto> create(@RequestBody @Valid WaitingListRequestDto request) {
        WaitingListResponseDto waitingList = service.create(request);
        return new ResponseEntity<>(waitingList, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WaitingListResponseDto> update(@PathVariable Long id, @RequestBody @Valid WaitingListRequestDto request) {
        WaitingListResponseDto waitingList = service.update(new WaitingListId(id), request);
        return ResponseEntity.ok(waitingList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(new WaitingListId(id));
        return ResponseEntity.noContent().build();
    }
}
