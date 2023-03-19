package com.codelap.api.controller.study.dto;

public class StudyRemoveMemberDto {
    public record StudyRemoveRequest(
            Long studyId,
            Long memberId,
            Long leaderId
    ) {
    }
}
