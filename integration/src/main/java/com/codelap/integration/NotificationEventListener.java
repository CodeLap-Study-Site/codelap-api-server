package com.codelap.integration;

import com.codelap.common.support.AsyncDiscordEvent;
import com.codelap.common.support.DiscordEvent;
import com.codelap.integration.discord.DiscordApi;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final DiscordApi discordApi;

    @EventListener(DiscordEvent.class)
    public void onSlackEvent(DiscordEvent event) {
        discordApi.send(event.getMessage());
    }

    @Async
    @TransactionalEventListener(AsyncDiscordEvent.class)
    public void onAsyncSlackEvent(AsyncDiscordEvent event) {
        discordApi.send(event.getMessage());
    }
}
