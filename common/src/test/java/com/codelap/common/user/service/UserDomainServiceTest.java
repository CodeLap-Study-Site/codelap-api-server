package com.codelap.common.user.service;

import com.codelap.common.support.BaseServiceTest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codelap.common.user.domain.UserStatus.DELETED;
import static org.assertj.core.api.Assertions.assertThat;

class UserDomainServiceTest extends BaseServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        user = userRepository.save(User.create("name", 10, career, "abcd", "setup"));
    }

    @Test
    void 유저_생성() {
        UserCareer career = UserCareer.create("직무", 10);

        User user = userService.create("이름", 10, career, "abcd", "email");

        User foundUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(foundUser.getId()).isNotNull();
    }

    @Test
    void 유저_수정() {
        UserCareer updatedCareer = UserCareer.create("업데이트된_직무", 11);

        userService.update(user.getId(), "updatedName", 11, updatedCareer);

        assertThat(user.getName()).isEqualTo("updatedName");
        assertThat(user.getAge()).isEqualTo(11);
        assertThat(user.getCareer()).isSameAs(updatedCareer);
    }

    @Test
    void 유저_비밀번호_변경() {
        userService.changePassword(user.getId(), user.getPassword(), "changePassword");

        assertThat(user.getPassword()).isEqualTo("changePassword");
    }

    @Test
    void 유저_삭제() {
        userService.delete(user.getId());

        assertThat(user.getStatus()).isEqualTo(DELETED);
    }
}