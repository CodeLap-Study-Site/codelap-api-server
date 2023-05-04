package com.codelap.api.service.user;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserAppService implements UserAppService {

    private final UserRepository userRepository;
    private final UserQueryAppService userQueryAppService;

    @Override
    public boolean getDuplicateCheckByName(String name) {
        return userQueryAppService.getDuplicateCheckByName(name);
    }

    @Override
    public boolean isActivated(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.isActivated();
    }
}
