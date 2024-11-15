package com.wora.waitingroom.waitinglist.application.service;

import com.wora.waitingroom.waitinglist.application.dto.response.AnalyticsResponseDto;
import com.wora.waitingroom.waitinglist.domain.vo.WaitingListId;

public interface AnalyticsService {
    AnalyticsResponseDto calculateAnalytics(WaitingListId id);
}
