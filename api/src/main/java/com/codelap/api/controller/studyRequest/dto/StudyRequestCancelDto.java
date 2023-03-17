package com.codelap.api.controller.studyRequest.dto;

public class StudyRequestCancelDto {
    public record StudyRequestCancelRequest(
            Long studyRequestId,
            Long userId
    ){
    }
}
