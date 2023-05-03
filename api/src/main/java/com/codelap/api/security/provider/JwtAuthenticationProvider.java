package com.codelap.api.security.provider;

import com.codelap.api.security.component.JwtComponent;
import com.codelap.api.security.user.DefaultCodeLapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

import static com.codelap.api.security.component.JwtType.ACCESS;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtComponent jwtComponent;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String token = (String) authentication.getPrincipal();

        jwtComponent.validate(token, ACCESS);

        Long id = jwtComponent.getId(token);

        return new UsernamePasswordAuthenticationToken(new DefaultCodeLapUser(id), token, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

