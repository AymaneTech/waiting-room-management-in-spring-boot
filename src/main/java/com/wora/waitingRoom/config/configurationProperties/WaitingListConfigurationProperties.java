package com.wora.waitingRoom.config.configurationProperties;

import com.wora.waitingRoom.waitingList.domain.valueObject.Algorithm;
import com.wora.waitingRoom.waitingList.domain.valueObject.Mode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.waiting-list.default")
public record WaitingListConfigurationProperties(
        @DefaultValue("15")
        Integer capacity,

        @DefaultValue("PART_TIME")
        Mode mode,

        @DefaultValue("HPF")
        Algorithm algorithm
) {
}