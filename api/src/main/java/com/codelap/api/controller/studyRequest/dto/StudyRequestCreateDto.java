package com.codelap.api.controller.studyRequest.dto;

import com.codelap.common.study.domain.StudyDifficulty;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;

import java.time.OffsetDateTime;

public class StudyRequestCreateDto {

    public record StudyRequestCreateRequest(
            Long userId,
            Long studyId,
            String message
    ) {
    }
}
