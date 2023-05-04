package com.codelap.api.controller.user.dto;

import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserTechStack;

import java.util.List;

public class UserActivateDto {

    public record UserActivateRequest(
            String name,
            UserActivateRequestUserCareerDto career,
            List<UserTechStack> techStacks
    ) {
    }

    public record UserActivateRequestUserCareerDto(
            String occupation,
            int year
    ) {
        public UserCareer toCareer() {
            return UserCareer.create(occupation, year);
        }
    }
}
