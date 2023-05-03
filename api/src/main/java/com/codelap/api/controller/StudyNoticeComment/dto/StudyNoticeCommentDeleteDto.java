package com.codelap.api.controller.StudyNoticeComment.dto;

public class StudyNoticeCommentDeleteDto {
    public record StudyNoticeCommentDeleteRequest(
            Long studyNoticeCommentId,
            Long userId
    ) {
    }
}
