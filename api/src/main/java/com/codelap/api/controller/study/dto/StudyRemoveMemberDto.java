package com.codelap.api.controller.study.dto;

public class StudyRemoveMemberDto {
    public record StudyRemoveMemberRequest(
            Long studyId,
            Long memberId
    ) {
    }
}
