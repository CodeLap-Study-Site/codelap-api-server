package com.codelap.api.controller.study.dto;

import com.codelap.common.study.domain.StudyDifficulty;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyTechStack;

import java.time.OffsetDateTime;
import java.util.List;

public class StudyUpdateDto {

    public record StudyUpdateRequest(
            Long studyId,
            Long leaderId,
            String name,
            String info,
            int maxMembersSize,
            StudyDifficulty difficulty,
            StudyUpdateRequestStudyPeriodDto period,
            StudyUpdateRequestStudyNeedCareerDto career,
            List<StudyTechStack> techStackList
    ) {
    }

    public record StudyUpdateRequestStudyPeriodDto(
            OffsetDateTime startAt,
            OffsetDateTime endAt
    ) {
        public StudyPeriod toStudyPeriod() {
            return StudyPeriod.create(startAt, endAt);
        }
    }

    public record StudyUpdateRequestStudyNeedCareerDto(
            String occupation,
            int year
    ) {
        public StudyNeedCareer toStudyNeedCareer() {
            return StudyNeedCareer.create(occupation, year);
        }
    }
}
