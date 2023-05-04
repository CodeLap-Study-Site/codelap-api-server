package com.codelap.api.security.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import static org.springframework.security.oauth2.core.OAuth2ErrorCodes.SERVER_ERROR;

@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

            OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            String userNameAttributeName = userRequest.getClientRegistration()
                    .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

            log.info("registrationId = {}", registrationId);
            log.info("userNameAttributeName = {}", userNameAttributeName);

            OAuth2Attribute oAuth2Attribute =
                    OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

            var memberAttribute = oAuth2Attribute.convertToMap();

            return new DefaultOAuth2User(null, memberAttribute, "socialId");
        } catch (Exception ex) {
            ex.printStackTrace();

            OAuth2Error oAuth2Error = new OAuth2Error(SERVER_ERROR);

            throw new OAuth2AuthenticationException(oAuth2Error, ex.getMessage());
        }
    }
}
