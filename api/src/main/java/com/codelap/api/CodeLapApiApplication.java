package com.codelap.api;

import com.codelap.common.support.DiscordEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@RequiredArgsConstructor
public class CodeLapApiApplication {

    private final ApplicationEventPublisher eventPublisher;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        eventPublisher.publishEvent(new DiscordEvent("CodeLap Api Started"));
    }

    public static void main(String[] args) {
        SpringApplication.run(CodeLapApiApplication.class, args);
    }
}
