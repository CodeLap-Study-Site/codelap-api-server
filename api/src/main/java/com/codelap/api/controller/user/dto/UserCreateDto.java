package com.codelap.api.controller.user.dto;

import com.codelap.common.user.domain.UserCareer;

public class UserCreateDto {

    public record UserCreateRequest(
            String name,
            int age,
            UserCreateRequestUserCareerDto career,
            String password,
            String checkPassword
    ){}

    public record UserCreateRequestUserCareerDto(
            String occupation,
            int year
    ){
        public UserCareer toCareer(){
            return UserCareer.create(occupation, year);
        }
    }
}
