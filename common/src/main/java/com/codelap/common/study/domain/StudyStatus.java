package com.codelap.common.study.domain;

import java.util.Set;

public enum StudyStatus {
    OPENED,
    IN_PROGRESS,
    CLOSED,
    DELETED;

    public final static Set<StudyStatus> CAN_OPEN_STATUSES = Set.of(IN_PROGRESS, CLOSED);
    public final static Set<StudyStatus> CAN_CLOSED_STATUSES = Set.of(IN_PROGRESS, OPENED);

    public final static Set<StudyStatus> CAN_DELETE_STATUSES = Set.of(IN_PROGRESS, OPENED, CLOSED);
}
