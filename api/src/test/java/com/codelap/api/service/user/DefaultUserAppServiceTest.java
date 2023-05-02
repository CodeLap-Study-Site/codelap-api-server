package com.codelap.api.service.user;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class DefaultUserAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAppService userAppService;

    private User user;

    @Test
    void 닉네임_중복_체크_성공() {
        String name = "name";

        boolean result = userAppService.getDuplicateCheckByName(name);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    void 닉네임_중복_체크_실패__중복_이름_존재재() {

    }

}
