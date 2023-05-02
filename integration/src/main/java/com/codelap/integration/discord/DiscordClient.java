package com.codelap.integration.discord;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;

@AllArgsConstructor
public class DiscordClient {
    private String webhookUrl;
    private String avatar;
    private String username;

    public void send(String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, "application/json");

        DiscordWebhookMessage discordWebhookMessage = new DiscordWebhookMessage(username, avatar, message);
        HttpEntity<DiscordWebhookMessage> entity = new HttpEntity<>(discordWebhookMessage, headers);

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.exchange(webhookUrl, POST, entity, String.class);
    }
}
