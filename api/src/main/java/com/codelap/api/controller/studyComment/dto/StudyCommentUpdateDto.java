package com.codelap.api.controller.studyComment.dto;

public class StudyCommentUpdateDto {
    public record StudyCommentUpdateRequest(
            Long studyCommentId,
            String message
    ) {
    }
}
