package com.codelap.api.controller.study.dto;

public class StudyAddMemberDto {

    public record StudyAddMemberRequest(
            Long studyId,
            Long userId,
            Long leaderId

    ) {

    }
}