package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetAllStudiesStudyDto;
import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;

import java.util.List;

public interface StudyAppService {

    List<GetStudiesStudyDto> getStudies(Long userId);

    List<GetAllStudiesStudyDto> getAllStudies();
}
