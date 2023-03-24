package com.codelap.api.service.user;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserJpaRepository;
import com.codelap.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserAppService implements UserAppService{

    private final UserJpaRepository userJpaRepository;
    private final UserRepository userRepository;


    @Override
    public List<Study> findStudyListByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return userJpaRepository.findStudyListByUserId(user);
    }
}
