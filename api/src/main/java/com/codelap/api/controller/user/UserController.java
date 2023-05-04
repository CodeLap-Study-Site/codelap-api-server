package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequest;
import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.api.service.user.UserAppService;
import com.codelap.common.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserAppService userAppService;

    @PostMapping("/activate")
    public void activate(
            @RequestBody UserUpdateRequest req,
            @AuthenticationPrincipal DefaultCodeLapUser user
    ) {
        userService.activate(user.getId(), req.name(), req.career().toCareer(), req.techStacks());
    }

    @PostMapping("/update")
    public void update(
            @RequestBody UserUpdateRequest req,
            @AuthenticationPrincipal DefaultCodeLapUser user
    ) {
        userService.update(user.getId(), req.name(), req.career().toCareer(), req.techStacks());
    }


    @DeleteMapping
    public void delete(
            @AuthenticationPrincipal DefaultCodeLapUser user
    ) {
        userService.delete(user.getId());
    }

    @GetMapping("/is-activated")
    public boolean isActivated(
            @AuthenticationPrincipal DefaultCodeLapUser user
    ) {
        return userAppService.isActivated(user.getId());
    }
}
