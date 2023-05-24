package com.codelap.api.controller.study.dto;

import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import com.codelap.common.support.TechStack;

import java.util.List;
import java.util.stream.Collectors;

public class GetMyStudiesDto {

    public record GetMyStudiesResponse(
            List<GetStudiesDto> studies
    ) {
        public static GetMyStudiesResponse create(List<GetStudyInfo> studies) {
            return new GetMyStudiesResponse(studies.stream()
                    .map(study -> GetStudiesDto.create(study))
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
            List<TechStack> techStackList

    ) {
        public static List<TechStack> toTechStackList(List<GetStudiesCardDto.GetTechStackInfo> techStackList) {
            return techStackList.stream().map(GetStudiesCardDto.GetTechStackInfo::getTechStack).collect(Collectors.toList());
        }

        public static GetStudiesDto create(GetStudyInfo study) {
            return new GetStudiesDto(study.getStudyId(), study.getStudyName(),
                    study.getStudyPeriod(), study.getLeaderName(),
                    study.getCommentCount(), study.getViewCount(), study.getBookmarkCount(),
                    study.getMaxMemberSize(), toTechStackList(study.getTechStackList()));
        }
    }
}