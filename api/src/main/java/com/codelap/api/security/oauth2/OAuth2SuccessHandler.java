package com.codelap.api.security.oauth2;

import com.codelap.api.security.component.JwtComponent;
import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static com.codelap.api.security.component.JwtType.ACCESS;
import static com.codelap.api.security.component.JwtType.REFRESH;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtComponent jwtComponent;
    @Value("${oauth2.frontUri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Object attribute = oAuth2User.getAttribute("socialId");
        Long socialId = Long.parseLong(Objects.requireNonNull(attribute).toString());

        User user = userService.loadUser(socialId);

        String accessToken = jwtComponent.issue(user.getId(), user.getEmail(), ACCESS);
        String refreshToken = jwtComponent.issue(user.getId(), user.getEmail(), REFRESH);

        String targetUrl = getTargetUrl(accessToken, refreshToken);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                new DefaultCodeLapUser(user.getId()), null, new ArrayList<>()
        );

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String getTargetUrl(String accessToken, String refreshToken) {

        return UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();

    }
}
