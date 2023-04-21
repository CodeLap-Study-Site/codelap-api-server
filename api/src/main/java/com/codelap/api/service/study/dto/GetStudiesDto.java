package com.codelap.api.service.study.dto;

import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.dto.GetTechStackDto;

import java.util.List;

public class GetStudiesDto {

    public record GetStudiesStudyDto(
            String studyName,
            StudyPeriod studyPeriod,
            String leaderName,
            Long commentCount,
            Long viewCount,
            Long bookmarkCount,
            int maxMemberSize,
            List<GetTechStackDto> techStackList
    ) {
    }
}
