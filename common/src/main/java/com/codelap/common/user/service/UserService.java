package com.codelap.common.user.service;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserTechStack;

import java.util.List;

public interface UserService {

    User loadUser(Long socialId);

    void activate(Long userId, String name, UserCareer career, List<UserTechStack> techStacks);

    void update(Long userId, String name, UserCareer career, List<UserTechStack> techStacks);

    void delete(Long userId);
}
