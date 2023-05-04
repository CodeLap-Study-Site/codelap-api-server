package com.codelap.common.studyComment.domain;

import java.util.Set;

public enum StudyCommentStatus {
    CREATED,
    DELETED;

    public final static Set<StudyCommentStatus> CAN_DELETE_STATUS = Set.of(CREATED);
}
