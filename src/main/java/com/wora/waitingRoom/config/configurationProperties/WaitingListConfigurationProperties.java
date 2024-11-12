package com.wora.waitingRoom.waitingList.infrastructure;

import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.waiting-list.default")
public record WaitingListConfigurationValues(
        Integer capacity,
        Mode mode,
        Algorithm algorithm
) {
}
