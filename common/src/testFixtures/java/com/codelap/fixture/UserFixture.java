package com.codelap.fixture;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserTechStack;

import java.util.List;

import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.user.domain.UserStatus.ACTIVATED;

public class UserFixture {
    public static User createUser() {
        return User.create(1L);
    }

    public static User createActivateUser() {
        User user = User.create(2L);

        user.setStatus(ACTIVATED);

        return user;
    }

    public static User createActivateUser(String name) {
        User user = User.create(2L);
        UserTechStack techStack = new UserTechStack(Java);

        user.activate(name, createUserCareer(), List.of(techStack));

        return user;
    }

    public static UserCareer createUserCareer() {
        return UserCareer.create("occupation", 1);
    }
}
