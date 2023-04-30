package com.codelap.api.controller.study.dto;

import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;

import java.util.List;
import java.util.stream.Collectors;

public class GetMyStudiesDto {

    public record GetMyStudiesResponse(
            List<GetStudiesDto> studies
    ) {
        public static GetMyStudiesResponse create(List<GetStudyInfo> studies) {
            return new GetMyStudiesResponse(studies.stream()
                    .map(GetStudiesDto::create)
                    .collect(Collectors.toList()));
        }
    }

    public record GetStudiesDto(
            Long studyId,
            String studyName,
            StudyPeriod studyPeriod,
            String leaderName,
            Long commentCount,
            Long viewCount,
            Long bookmarkCount,
            int maxMemberSize,
            List<GetStudiesCardDto.GetTechStackInfo> techStackList

    ) {
        public static GetStudiesDto create(GetStudyInfo study) {
            return new GetStudiesDto(study.getStudyId(), study.getStudyName(),
                    study.getStudyPeriod(), study.getLeaderName(),
                    study.getCommentCount(), study.getViewCount(), study.getBookmarkCount(),
                    study.getMaxMemberSize(), study.getTechStackList());
        }
    }
}