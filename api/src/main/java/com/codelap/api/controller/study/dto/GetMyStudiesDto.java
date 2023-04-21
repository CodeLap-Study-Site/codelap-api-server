package com.codelap.api.controller.study.dto;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.dto.GetTechStackDto;

import java.util.List;
import java.util.stream.Collectors;

public class GetMyStudiesDto {

    public record GetMyStudiesResponse(
            List<GetStudiesDto> studies

    ) {
        public static GetMyStudiesResponse create(List<GetStudiesStudyDto> studies) {
            return new GetMyStudiesResponse(studies.stream()
                    .map(GetStudiesDto::create)
                    .collect(Collectors.toList()));
        }
    }

    public record GetStudiesDto(
            String studyName,
            StudyPeriod studyPeriod,
            String leaderName,
            Long commentCount,
            Long viewCount,
            Long bookmarkCount,
            int maxMemberSize,
            List<GetTechStackDto> techStackList
    ) {

        public static GetStudiesDto create(GetStudiesStudyDto study) {
            return new GetStudiesDto(
                    study.studyName(), study.studyPeriod(), study.leaderName(), study.commentCount(),
                    study.viewCount(), study.bookmarkCount(), study.maxMemberSize(), study.techStackList()
            );
        }
    }
}