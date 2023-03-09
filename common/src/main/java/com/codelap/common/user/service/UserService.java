package com.codelap.common.user.service;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;

public interface UserService {

    User create(String name, int age, UserCareer career);
}
