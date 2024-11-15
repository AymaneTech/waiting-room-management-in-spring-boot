package com.wora.waitingroom.config.configurationProperties;

import com.wora.waitingroom.waitinglist.domain.vo.Algorithm;
import com.wora.waitingroom.waitinglist.domain.vo.Mode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.context.annotation.Profile;

@Profile("prod")
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