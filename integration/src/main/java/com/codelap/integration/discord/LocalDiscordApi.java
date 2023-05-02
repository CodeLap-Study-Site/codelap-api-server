package com.codelap.integration.discord;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"local", "default"})
public class LocalDiscordApi implements DiscordApi {

    @Override
    public void send(String message) {
    }
}
