package com.codelap.api.controller.StudyNoticeComment.dto;

public class StudyNoticeCommentCreateDto {
    public record StudyNoticeCommentCreateRequest(
            Long studyNoticeId,
            Long userId,
            String content
    ) {
    }
}
