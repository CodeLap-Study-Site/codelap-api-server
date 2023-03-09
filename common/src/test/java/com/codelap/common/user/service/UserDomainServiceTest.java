package com.codelap.common.user.service;

import com.codelap.common.support.BaseServiceTest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class UserDomainServiceTest extends BaseServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저_생성() {
        UserCareer career = UserCareer.create("직무", 10);

        User user = userService.create("이름", 10, career);

        User foundUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(foundUser.getId()).isNotNull();
    }
}