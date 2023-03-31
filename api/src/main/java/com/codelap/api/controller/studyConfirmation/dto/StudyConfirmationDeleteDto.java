package com.codelap.api.controller.studyConfirmation.dto;

public class StudyConfirmationDeleteDto {

    public record StudyConfirmationDeleteRequest(
            Long studyConfirmId,
            Long userId
    ) {
    }
}
