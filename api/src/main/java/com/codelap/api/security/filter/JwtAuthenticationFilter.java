package com.codelap.api.security.filter;

import com.codelap.api.security.component.JwtComponent;
import com.codelap.api.security.user.CodeLapUserException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

import static com.codelap.api.security.component.JwtType.ACCESS;
import static com.codelap.api.security.component.JwtType.REFRESH;
import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtComponent jwtComponent;
    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var accessToken = jwtComponent.resolveToken(request, ACCESS);

        try {
            if (nonNull(accessToken)) {
                accessToken = checkRefreshFlow(request, response, accessToken);

                val authenticationToken = new UsernamePasswordAuthenticationToken(accessToken, "", new ArrayList<>());

                val authentication = authenticationManager.authenticate(authenticationToken);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (CodeLapUserException ex) {
            ex.printStackTrace();

            SecurityContextHolder.clearContext();

            response.sendError(ex.getStatus().value(), ex.getMessage());

            return;
        }

        filterChain.doFilter(request, response);
    }

    private String checkRefreshFlow(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        var refreshToken = jwtComponent.resolveToken(request, REFRESH);

        if (nonNull(refreshToken)) {
            jwtComponent.isExpired(accessToken);

            jwtComponent.validate(refreshToken, REFRESH);

            val id = jwtComponent.getId(accessToken);
            val audience = jwtComponent.getAudience(accessToken);

            accessToken = jwtComponent.issue(id, audience, ACCESS);
            refreshToken = jwtComponent.issue(id, audience, REFRESH);

            response.addHeader("Authorization", "Bearer " + accessToken);
            response.addHeader("X-Refresh-Token", "Bearer " + refreshToken);
        }

        return accessToken;
    }
}
