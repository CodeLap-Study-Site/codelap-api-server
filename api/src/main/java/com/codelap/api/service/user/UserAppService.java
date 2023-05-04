package com.codelap.api.service.user;

public interface UserAppService {
    boolean getDuplicateCheckByName(String name);

    boolean isActivated(Long userId);
}
