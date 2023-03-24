package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequest;
import com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequest;
import com.codelap.api.service.user.UserAppService;
import com.codelap.common.study.domain.Study;
import com.codelap.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.codelap.api.controller.user.dto.FindStudyListByUserIdDto.FindStudyListByUserIdRequest;
import static com.codelap.api.controller.user.dto.UserChangePasswordDto.UserChangePasswordRequest;
import static com.codelap.api.controller.user.dto.UserDeleteDto.UserDeleteRequest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserAppService userAppService;

    @PostMapping
    public void create(
            @RequestBody UserCreateRequest req
    ) {
        userService.create(req.name(), req.age(), req.career().toCareer(), req.password(), req.email());
    }

    @PostMapping("/update")
    public void update(
            @RequestBody UserUpdateRequest req
    ) {
        userService.update(req.userId(), req.name(), req.age(), req.career().toCareer());
    }

    @PostMapping("/change-password")
    public void changePassword(
            @RequestBody UserChangePasswordRequest req
    ) {
        userService.changePassword(req.userId(), req.password(), req.newPassword());
    }

    @DeleteMapping
    public void delete(
            @RequestBody UserDeleteRequest req
    ) {
        userService.delete(req.userId());
    }

    @GetMapping("findStudyList")
    public List<Study> findStudyListByUserId(
            @RequestBody FindStudyListByUserIdRequest req
    ) {
        return userAppService.findStudyListByUserId(req.userId());
    }
}
