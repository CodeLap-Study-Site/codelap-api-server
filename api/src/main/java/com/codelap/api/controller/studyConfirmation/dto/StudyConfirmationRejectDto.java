package com.codelap.api.controller.studyConfirmation.dto;

public class StudyConfirmationRejectDto {
    public record StudyConfirmationRejectRequest(
            Long studyConfirmId,
            Long leaderId
    ) {
    }
}
