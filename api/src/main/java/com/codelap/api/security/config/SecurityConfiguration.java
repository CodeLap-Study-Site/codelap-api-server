package com.codelap.api.security.config;

import com.codelap.api.security.oauth2.CustomOAuth2UserService;
import com.codelap.api.security.oauth2.OAuth2SuccessHandler;
import com.codelap.api.security.provider.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@Profile({"prod", "dev"})
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService oAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationProvider jwtAuthenticationProvider,
            SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> jwtFilterConfigurer
    ) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/auth/**", "/user/create", "/email-auth/**").permitAll()
                .anyRequest().authenticated();

        http.formLogin().disable();
        http.httpBasic().disable();
        http.cors().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authenticationProvider(jwtAuthenticationProvider);
        http.apply(jwtFilterConfigurer);

        http.oauth2Login()
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint().userService(oAuth2UserService);

        return http.build();
    }

    //TODO Front Page 추가 이후 CORS 설정 필요
}
