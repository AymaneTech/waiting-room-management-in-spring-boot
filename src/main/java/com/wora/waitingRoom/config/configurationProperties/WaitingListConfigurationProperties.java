package com.wora.waitingRoom.config.configurationProperties;

import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.waiting-list.default")
public record WaitingListConfigurationProperties(
        Integer capacity,
        Mode mode,
        Algorithm algorithm
) {
}
