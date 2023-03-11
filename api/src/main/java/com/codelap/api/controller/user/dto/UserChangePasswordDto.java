package com.codelap.api.controller.user.dto;

public class UserChangePasswordDto {

    public record UserChangePasswordRequest(
            Long userId,
            String password,
            String newPassword
    ) {
    }
}
