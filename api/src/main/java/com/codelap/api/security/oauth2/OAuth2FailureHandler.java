package com.codelap.api.security.oauth2;

import com.codelap.common.support.UnhandledExceptionEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ApplicationEventPublisher eventPublisher;
    @Value("${oauth2.frontUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException exception
    ) throws IOException {
        eventPublisher.publishEvent(new UnhandledExceptionEvent(exception));

        String targetUrl = getTargetUrl();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String getTargetUrl() {

        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("error", "")
                .build().toUriString();
    }
}
