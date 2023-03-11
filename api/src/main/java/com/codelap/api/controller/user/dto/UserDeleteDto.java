package com.codelap.api.controller.user.dto;

public class UserDeleteDto {

    public record UserDeleteRequest(
            Long userId
    ) {
    }
}
