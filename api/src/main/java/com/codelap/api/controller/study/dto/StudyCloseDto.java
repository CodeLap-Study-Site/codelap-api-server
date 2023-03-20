package com.codelap.api.controller.study.dto;

public class StudyCloseDto {
    public record StudyCloseRequest(
            Long studyId,
            Long leaderId
    ){
    }
}
