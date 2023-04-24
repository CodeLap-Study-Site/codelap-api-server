package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetOpenedStudiesDto;

import java.util.List;

public interface StudyAppService {

    List<GetStudiesStudyDto> getStudies(Long userId);

    List<GetStudiesStudyDto> getAttendedStudiesByUser(Long userId);

    public List<GetOpenedStudiesDto> getOpenedStudies();
}
