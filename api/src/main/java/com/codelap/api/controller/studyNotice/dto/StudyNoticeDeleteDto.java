package com.codelap.api.controller.studyNotice.dto;

public class StudyNoticeDeleteDto {
    public record StudyNoticeDeleteRequest(
            Long studyNoticeId,
            Long leaderId
    ) {
    }
}
