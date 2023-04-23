package com.codelap.api.service.study.dto;

import com.codelap.common.study.dto.GetMyStudiesDto.GetStudyInfo;

public class GetStudiesDto {

    public record GetStudiesStudyDto(
            GetStudyInfo getMyStudiesDto
    ) {
    }
}
