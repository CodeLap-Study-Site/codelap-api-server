package com.codelap.api.service.user;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class DefaultUserAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAppService userAppService;

    private User user;

    @Test
    void 닉네임_중복_체크_성공_닉네임_있음() {
        UserCareer career = UserCareer.create("직무", 1);
        userRepository.save(User.create("member", 10, career, "abcd", "member"));

        boolean result = userAppService.getDuplicateCheckByName("member");

        List<User> nameList = userRepository.findAll().stream()
                .filter(user -> user.getName() != null)
                .collect(Collectors.toList());

        Assertions.assertThat(result).isTrue();
        Assertions.assertThat(nameList.contains("member"));
    }

    @Test
    void 닉네임_중복_체크_실패__중복_이름_없음() {
        String name = "name";

        boolean result = userAppService.getDuplicateCheckByName(name);

        Assertions.assertThat(result).isFalse();}
}
