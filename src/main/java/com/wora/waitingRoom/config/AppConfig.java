package com.wora.waitingRoom.config;

import com.wora.waitingRoom.config.configurationProperties.OpenApiConfigurationProperties;
import com.wora.waitingRoom.config.configurationProperties.WaitingListConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        WaitingListConfigurationProperties.class,
        OpenApiConfigurationProperties.class
})
public class AppConfig {
}
