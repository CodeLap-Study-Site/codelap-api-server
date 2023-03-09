package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public void create(
            @RequestBody UserCreateRequest req
    ) {
        userService.create(req.name(), req.age(), req.career().toCareer());
    }
}
