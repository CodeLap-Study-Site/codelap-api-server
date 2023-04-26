package com.codelap.api.service.study.dto;

import com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;

public class GetStudiesDto {

    public record GetStudiesStudyDto(
            GetStudyInfo getMyStudiesDto
    ) {
    }
}
