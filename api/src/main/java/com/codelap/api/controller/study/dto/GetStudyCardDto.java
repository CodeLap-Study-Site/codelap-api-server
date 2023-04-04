package com.codelap.api.controller.study.dto;

import com.codelap.api.service.study.dto.GetAllStudiesStudyDto;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.support.TechStack;

import java.util.List;
import java.util.stream.Collectors;

public class GetStudyCardDto {

    public record GetStudyCardResponse(
            List<GetStudyCardResponseStudyDto> studies

    ) {
        public static GetStudyCardResponse create(List<GetAllStudiesStudyDto> studies) {
            return new GetStudyCardResponse(studies.stream()
                    .map(GetStudyCardResponseStudyDto::create)
                    .collect(Collectors.toList()));
        }
    }

    public record GetStudyCardResponseStudyDto(
            Long id,
            String name,
            StudyPeriod period,
            String leader,
            Long commentCount,
            Long viewCount,
            Long bookmarkCount,
            int memberCount,
            int maxMembersSize,
            List<TechStack> techStackList
    ) {

        public static GetStudyCardResponseStudyDto create(GetAllStudiesStudyDto study) {
            return new GetStudyCardResponseStudyDto(
                    study.getId(), study.getName(), study.getPeriod(), study.getLeader(), study.getCommentCount(), study.getViewCount(),
                    study.getBookmarkCount(), study.getMemberCount(), study.getMaxMembersSize(), study.getTechStackList()
            );
        }
    }
}