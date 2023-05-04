package com.codelap.common.studyNoticeComment.domain;

import java.util.Set;

public enum StudyNoticeCommentStatus {
    CREATED,
    DELETED;

    public final static Set<StudyNoticeCommentStatus> CAN_DELETE_STATUS = Set.of(CREATED);
}
