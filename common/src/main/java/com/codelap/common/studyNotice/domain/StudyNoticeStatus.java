package com.codelap.common.studyNotice.domain;

import com.codelap.common.study.domain.StudyStatus;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus;

import java.util.Set;

public enum StudyNoticeStatus {
    CREATED,
    DELETED;

    public final static Set<StudyNoticeStatus> CAN_DELETE_STATUS = Set.of(CREATED);

    }
