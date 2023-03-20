package com.codelap.api.controller.study.dto;

public class StudyLeaveDto {
    public record StudyLeaveRequest(
            Long studyId,
            Long memberId
    ) {
    }
}
