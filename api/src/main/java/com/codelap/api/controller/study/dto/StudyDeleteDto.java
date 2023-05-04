package com.codelap.api.controller.study.dto;

public class StudyDeleteDto {
    public record StudyDeleteRequest(
            Long studyId
    ) {
    }
}
