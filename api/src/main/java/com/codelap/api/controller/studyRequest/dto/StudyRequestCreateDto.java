package com.codelap.api.controller.studyRequest.dto;

public class StudyRequestCreateDto {

    public record StudyRequestCreateRequest(
            Long userId,
            Long studyId,
            String message
    ) {
    }
}
