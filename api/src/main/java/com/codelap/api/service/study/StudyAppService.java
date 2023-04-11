package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetMyStudiesDto;
import com.codelap.common.user.domain.User;

import java.util.List;

public interface StudyAppService {

    List<GetStudiesStudyDto> getStudies(Long userId);

    List<GetMyStudiesDto> getAllStudies(User user);
}
