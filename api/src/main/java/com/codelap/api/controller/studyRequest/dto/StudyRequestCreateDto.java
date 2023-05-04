package com.codelap.api.controller.studyRequest.dto;

public class StudyRequestCreateDto {

    public record StudyRequestCreateRequest(
            Long studyId,
            String message
    ) {
    }
}
