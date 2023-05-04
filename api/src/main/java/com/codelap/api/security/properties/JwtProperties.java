package com.codelap.api.security.properties;

import com.codelap.api.security.component.JwtType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Component
public class JwtProperties {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.accessTokenExpireTime}")
    private Long accessTokenExpireTime;

    @Value("${jwt.refreshTokenExpireTime}")
    private Long refreshTokenExpireTime;

    public String getEncodedSecretKey() {
        return Base64.getEncoder().encodeToString(secretKey.getBytes(UTF_8));
    }

    public Long getTokenExpireTime(JwtType jwtType) {
        Long expireTime = 0L;

        switch (jwtType) {
            case ACCESS -> expireTime = accessTokenExpireTime;
            case REFRESH -> expireTime = refreshTokenExpireTime;
        }

        return expireTime;
    }
}
