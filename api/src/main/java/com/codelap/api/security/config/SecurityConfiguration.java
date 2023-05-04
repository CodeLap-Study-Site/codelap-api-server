package com.codelap.api.security.config;

import com.codelap.api.security.oauth2.CustomOAuth2UserService;
import com.codelap.api.security.oauth2.OAuth2FailureHandler;
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
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@Profile({"prod", "dev"})
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationProvider jwtAuthenticationProvider,
            SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> jwtFilterConfigurer
    ) throws Exception {
        http.formLogin().disable();
        http.httpBasic().disable();
        http.cors().disable();
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/login/oauth2/authorization")
                .and()
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint().userService(oAuth2UserService);

        http.authorizeRequests()
                .anyRequest().authenticated();

        http.authenticationProvider(jwtAuthenticationProvider);
        http.apply(jwtFilterConfigurer);

        http.exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(FORBIDDEN));

        return http.build();
    }

    //TODO Front Page 추가 이후 CORS 설정 필요
}
