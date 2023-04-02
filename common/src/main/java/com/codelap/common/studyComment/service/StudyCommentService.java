package com.codelap.common.studyComment.service;

public interface StudyCommentService {
    void create(Long studyId, Long userId, String message);
}
