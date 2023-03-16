package com.codelap.api.controller.study.dto;

public class StudyProceedDto {
    public record StudyProceedRequest(
            Long studyId,
            Long leaderId
    ) {
    }
}
