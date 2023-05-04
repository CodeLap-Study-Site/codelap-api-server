package com.codelap.api.service.user;

import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codelap.fixture.UserFixture.createActivateUser;

@SpringBootTest
@Transactional
public class DefaultUserAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAppService userAppService;

    @Test
    void 닉네임_중복_체크_성공_닉네임_있음() {
        userRepository.save(createActivateUser("member"));

        Assertions.assertThat(userAppService.getDuplicateCheckByName("member")).isTrue();
        Assertions.assertThat(userAppService.getDuplicateCheckByName("fakeName")).isFalse();
    }
}
