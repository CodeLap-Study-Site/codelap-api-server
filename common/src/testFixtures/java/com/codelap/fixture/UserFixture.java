package com.codelap.fixture;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;

public class UserFixture {
    public static User createUser() {
        return User.create("name", 10, createUserCareer(), "1234", "email");
    }

    public static User createUser(String name) {
        return User.create(name, 10, createUserCareer(), "1234", "email");
    }

    public static User createUser(String name, String email) {
        return User.create(name, 10, createUserCareer(), "1234", email);
    }

    public static UserCareer createUserCareer() {
        return UserCareer.create("occupation", 1);
    }
}
