package com.wora.waitingroom.config;

import com.wora.waitingroom.config.configurationProperties.OpenApiConfigurationProperties;
import com.wora.waitingroom.config.configurationProperties.WaitingListConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        WaitingListConfigurationProperties.class,
        OpenApiConfigurationProperties.class
})
public class AppConfig {
}
