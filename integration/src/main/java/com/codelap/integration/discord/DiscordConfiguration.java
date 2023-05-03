package com.codelap.integration.discord;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "discord")
    public DiscordProperties discordProperties() {
        return new DiscordProperties();
    }

    @Bean
    public DiscordClient discordClient(DiscordProperties discordProperties) {
        return new DiscordClient(
                discordProperties.getWebhookUrl(),
                discordProperties.getAvatarUrl(),
                discordProperties.getUsername()
        );
    }
}
