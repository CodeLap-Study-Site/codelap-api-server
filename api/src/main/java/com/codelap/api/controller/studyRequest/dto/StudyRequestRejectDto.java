package com.codelap.api.controller.studyRequest.dto;

public class StudyRequestRejectDto {

    public record StudyRequestRejectRequest(
            Long studyRequestId,
            Long leaderId,
            String rejectMessage
    ) {
    }
}
