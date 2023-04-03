package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetAllStudiesStudyDto;
import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.user.domain.User;

import java.util.List;

public interface StudyQueryAppService {

    List<GetStudiesStudyDto> getStudies(User user);

    List<GetAllStudiesStudyDto> getAllStudies();
}
