package com.codelap.api.service.user;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(userAppService.getDuplicateCheckByName("member")).isTrue();
        assertThat(userAppService.getDuplicateCheckByName("fakeName")).isFalse();
    }

    @Test
    void 유저_활성화_상태_체크() {
        User user = userRepository.save(createActivateUser("member"));

        boolean result = userAppService.isActivated(user.getId());

        assertThat(result).isTrue();
    }

    @Test
    void 유저_업데이트() throws Exception {
        User user = userRepository.save(createActivateUser("member"));

        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        userAppService.update(user.getId(), user.getName(), user.getCareer(), user.getTechStacks(), file);

        userRepository.flush();

        user = userRepository.findBySocialId(user.getSocialId()).orElseThrow();

        assertThat(user.getFiles()).isNotNull();
    }
}
