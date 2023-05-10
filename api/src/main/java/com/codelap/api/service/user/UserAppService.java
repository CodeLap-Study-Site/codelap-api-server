package com.codelap.api.service.user;

import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserTechStack;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserAppService {
    boolean getDuplicateCheckByName(String name);

    boolean isActivated(Long userId);

    void update(Long userId, String name, UserCareer career, List<UserTechStack> techStacks, MultipartFile image) throws IOException;
}
