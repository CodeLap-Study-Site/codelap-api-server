package com.codelap.common.user.service;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;

public interface UserService {

    User create(String name, int age, UserCareer career, String password);

    void update(Long userId, String name, int age, String password, UserCareer career);
}
