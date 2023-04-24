package com.codelap.api.controller.study.dto;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetMyStudiesDto.GetStudyInfo;

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
            GetStudyInfo getMyStudiesDto
    ) {
        public static GetStudiesDto create(GetStudiesStudyDto study) {
            return new GetStudiesDto(study.getMyStudiesDto());
        }
    }
}