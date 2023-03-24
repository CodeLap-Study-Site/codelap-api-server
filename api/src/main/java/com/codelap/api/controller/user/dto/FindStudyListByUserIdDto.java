package com.codelap.api.controller.user.dto;

public class FindStudyListByUserIdDto {

    public record FindStudyListByUserIdRequest(
            Long userId
    ) {
    }
}
