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
    public User create(String name, int age, UserCareer career, String password, String email) {
        User user = User.create(name, age, career, password, email);

        return userRepository.save(user);
    }

    @Override
    public void update(Long userId, String name, int age, UserCareer career) {
        User user = userRepository.findById(userId).orElseThrow();

        user.update(name, age, career);
    }

    @Override
    public void changePassword(Long userId, String password, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow();

        user.changePassword(password, newPassword);
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        user.delete();
    }
}
