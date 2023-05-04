package com.codelap.common.user.service;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.common.user.domain.UserTechStack;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDomainService implements UserService {

    private final UserRepository userRepository;

    public User loadUser(Long socialId) {
        Optional<User> userOptional = userRepository.findBySocialId(socialId);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            User newUser = User.create(socialId);

            return userRepository.save(newUser);
        }
    }

    @Override
    public void activate(Long userId, String name, UserCareer career, List<UserTechStack> techStacks) {
        User user = userRepository.findById(userId).orElseThrow();

        user.activate(name, career, techStacks);
    }

    @Override
    public void update(Long userId, String name, UserCareer career, List<UserTechStack> techStacks) {
        User user = userRepository.findById(userId).orElseThrow();

        user.update(name, career, techStacks);
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        user.delete();
    }
}
