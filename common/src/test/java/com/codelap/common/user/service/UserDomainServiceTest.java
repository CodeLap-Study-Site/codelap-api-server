package com.codelap.common.user.service;

import com.codelap.common.support.BaseServiceTest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.common.user.domain.UserTechStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.user.domain.UserStatus.ACTIVATED;
import static com.codelap.common.user.domain.UserStatus.DELETED;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static com.codelap.fixture.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;

class UserDomainServiceTest extends BaseServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(createActivateUser());
    }

    @Test
    void 유저_생성() {
        User user = userService.loadUser(10L);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void 유저_활성화() {
        User user = userRepository.save(createUser());

        UserTechStack techStack = new UserTechStack(Java);
        UserCareer career = UserCareer.create("직무", 11);

        userService.activate(user.getId(), "name", career, List.of(techStack));

        assertThat(user.getStatus()).isEqualTo(ACTIVATED);
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getCareer()).isSameAs(career);
        assertThat(user.getTechStacks()).containsExactly(techStack);
    }

    @Test
    void 유저_수정() {
        UserTechStack updateTechStack = new UserTechStack(Java);
        UserCareer updatedCareer = UserCareer.create("업데이트된_직무", 11);

        userService.update(user.getId(), "updatedName", updatedCareer, List.of(updateTechStack));

        assertThat(user.getName()).isEqualTo("updatedName");
        assertThat(user.getCareer()).isSameAs(updatedCareer);
        assertThat(user.getTechStacks()).containsExactly(updateTechStack);
    }

    @Test
    void 유저_삭제() {
        userService.delete(user.getId());

        assertThat(user.getStatus()).isEqualTo(DELETED);
    }
}