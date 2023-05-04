package com.codelap.api.controller.StudyNoticeComment.dto;

public class StudyNoticeCommentUpdateDto {
    public record StudyNoticeCommentUpdateReqeust(
            Long studyNoticeCommentId,
            Long userId,
            String content
    ) {
    }
}
