package com.codelap.api.controller.study.dto;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.StudyStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GetMyStudiesDto {

    public record GetMyStudiesResponse(
            List<GetMyStudiesResponseStudyDto> studies

    ) {
        public static GetMyStudiesResponse create(List<GetStudiesStudyDto> studies) {
            return new GetMyStudiesResponse(studies.stream()
                    .map(GetMyStudiesResponseStudyDto::create)
                    .collect(Collectors.toList()));
        }
    }

    public record GetMyStudiesResponseStudyDto(
            Long id,
            String name,
            OffsetDateTime createdAt,
            StudyStatus status
    ) {

        public static GetMyStudiesResponseStudyDto create(GetStudiesStudyDto study) {
            return new GetMyStudiesResponseStudyDto(
                    study.id(), study.name(), study.createdAt(), study.status()
            );
        }
    }
}