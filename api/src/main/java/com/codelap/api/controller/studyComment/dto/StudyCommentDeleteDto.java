package com.codelap.api.controller.studyComment.dto;

public class StudyCommentDeleteDto {
    public record StudyCommentDeleteRequest(
        Long studyCommentId,
        Long userId
    ) {
    }

}
