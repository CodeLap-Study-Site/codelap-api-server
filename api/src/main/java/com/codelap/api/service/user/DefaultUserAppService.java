package com.codelap.api.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserAppService implements UserAppService {

    private final UserQueryAppService userQueryAppService;

    @Override
    public boolean getDuplicateCheckByName(String name) {
        return userQueryAppService.getDuplicateCheckByName(name);
    }
}
