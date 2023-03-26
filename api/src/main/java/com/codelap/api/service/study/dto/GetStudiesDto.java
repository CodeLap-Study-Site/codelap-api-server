package com.codelap.api.service.study.dto;

import com.codelap.common.study.domain.StudyStatus;

import java.time.OffsetDateTime;

public class GetStudiesDto {

    public record GetStudiesStudyDto(
            Long id,
            String name,
            OffsetDateTime createdAt,
            StudyStatus status
    ){}
}
