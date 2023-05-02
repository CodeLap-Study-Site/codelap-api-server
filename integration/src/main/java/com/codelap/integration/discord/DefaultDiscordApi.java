package com.codelap.integration.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev", "prod"})
@RequiredArgsConstructor
public class DefaultDiscordApi implements DiscordApi {

    private final DiscordClient discordClient;

    @Override
    public void send(String message) {
        discordClient.send(message);
    }
}
