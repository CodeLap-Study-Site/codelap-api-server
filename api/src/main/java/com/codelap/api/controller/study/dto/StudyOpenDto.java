package com.codelap.api.controller.study.dto;

import com.codelap.common.study.domain.StudyPeriod;

import java.time.OffsetDateTime;

public class StudyOpenDto {
    public record StudyOpenRequest(
            Long studyId,
            Long leaderId,
            StudyOpenRequestStudyPeriodDto period
    ) {
    }

    public record StudyOpenRequestStudyPeriodDto(
            OffsetDateTime startAt,
            OffsetDateTime endAt
    ) {
        public StudyPeriod toStudyPeriod() {
            return StudyPeriod.create(startAt, endAt);
        }
    }
}
