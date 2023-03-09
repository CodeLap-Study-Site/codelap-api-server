package com.codelap.common.user.service;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDomainService implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(String name, int age, UserCareer career) {
        User user = User.create(name, age, career);

        return userRepository.save(user);
    }
}
