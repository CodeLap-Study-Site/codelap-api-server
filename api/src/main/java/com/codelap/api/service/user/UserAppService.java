package com.codelap.api.service.user;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserAppService {
    boolean getDuplicateCheckByName(String name);

    boolean isActivated(Long userId);

    void imageUpload(Long userId, MultipartFile multipartFile);
}
