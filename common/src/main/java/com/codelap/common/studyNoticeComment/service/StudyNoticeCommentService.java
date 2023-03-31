package com.codelap.common.studyNoticeComment.service;

import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;

public interface StudyNoticeCommentService {

    StudyNoticeComment create(StudyNotice studyNotice, Long userId, String content);
}
