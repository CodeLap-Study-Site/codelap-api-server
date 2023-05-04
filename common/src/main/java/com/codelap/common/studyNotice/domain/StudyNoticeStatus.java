package com.codelap.common.studyNotice.domain;

import java.util.Set;

public enum StudyNoticeStatus {
    CREATED,
    DELETED;

    public final static Set<StudyNoticeStatus> CAN_DELETE_STATUS = Set.of(CREATED);

}
