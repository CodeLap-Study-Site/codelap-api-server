package com.codelap.api.controller.user.dto;

import com.codelap.common.user.domain.UserCareer;

public class UserUpdateDto {

    public record UserUpdateRequest(
            Long userId,
            String name,
            int age,
            String password,
            UserUpdateRequestUserCareerDto career
    ) {
    }

    public record UserUpdateRequestUserCareerDto(
            String occupation,
            int year
    ) {
        public UserCareer toCareer() {
            return UserCareer.create(occupation, year);
        }
    }
}
