package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserChangePasswordDto;
import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequest;
import com.codelap.api.controller.user.dto.UserDeleteDto;
import com.codelap.api.controller.user.dto.UserUpdateDto;
import com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.user.dto.UserChangePasswordDto.*;
import static com.codelap.api.controller.user.dto.UserDeleteDto.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public void create(
            @RequestBody UserCreateRequest req
    ) {
        userService.create(req.name(), req.age(), req.career().toCareer(), req.password());
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
    ){
        userService.changePassword(req.userId(), req.password(), req.newPassword());
    }

    @DeleteMapping
    public void delete(
            @RequestBody UserDeleteRequest req
    ){
        userService.delete(req.userId());
    }
}
