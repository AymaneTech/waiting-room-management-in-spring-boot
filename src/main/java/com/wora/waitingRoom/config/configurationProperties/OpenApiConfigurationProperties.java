package com.wora.waitingRoom.config.configurationProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.open-api")
public record OpenApiConfigurationProperties(
        Info info,
        Server server
) {
    public record Server(
            String url,
            String description
    ) {
    }

    public record Contact(
            String name,
            String email,
            String url
    ) {
    }

    public record Info(
            String title,
            String version,
            String description,
            Contact contact
    ) {
    }
}
