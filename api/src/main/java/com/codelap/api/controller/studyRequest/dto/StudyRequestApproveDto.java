package com.codelap.api.controller.studyRequest.dto;

public class StudyRequestApproveDto {
    public record StudyRequestApproveRequest(
            Long studyRequestId,
            Long leaderId
    ) {

    }
}
