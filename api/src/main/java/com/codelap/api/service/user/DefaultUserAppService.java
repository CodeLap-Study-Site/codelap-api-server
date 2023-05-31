package com.codelap.api.service.user;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserFile;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.integration.s3.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.codelap.common.user.domain.UserFile.create;
import static com.codelap.common.user.domain.UserFile.dirName;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserAppService implements UserAppService {

    private final UserRepository userRepository;
    private final UserQueryAppService userQueryAppService;
    private final FileUpload fileUpload;

    @Override
    public boolean getDuplicateCheckByName(String name) {
        return userQueryAppService.getDuplicateCheckByName(name);
    }

    @Override
    public boolean isActivated(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return user.isActivated();
    }

    @Override
    public void imageUpload(Long userId, MultipartFile multipartFile) {
        User user = userRepository.findById(userId).orElseThrow();
        user.changeImage(List.of((UserFile) fileUpload.upload(multipartFile, dirName, create())));
    }
}
