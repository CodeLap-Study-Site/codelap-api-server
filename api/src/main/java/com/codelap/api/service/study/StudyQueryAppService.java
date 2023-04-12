package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.TechStack;
import com.codelap.common.study.dto.GetMyStudiesDto;
import com.codelap.common.user.domain.User;

import java.util.List;

public interface StudyQueryAppService {

    List<GetStudiesStudyDto> getStudies(User user);

    List<GetMyStudiesDto> getAllStudies(User user);

    List<TechStack> findTechStack(Study study);
}
