package com.wora.waitingRoom.config;

import com.wora.waitingRoom.waitingList.infrastructure.WaitingListConfigurationValues;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WaitingListConfigurationValues.class)
public class AppConfig {
}
