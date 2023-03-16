package com.codelap.api.controller.study.dto;

public class StudyProceedRequest {
    public record StudyProceedRequestDto(
            Long studyId,
            Long leaderId
    ) {
    }
}
