package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.TechStack;
import com.codelap.common.study.dto.GetMyStudiesDto;
import com.codelap.common.user.domain.User;

import java.util.List;

public interface StudyQueryAppService {

    List<GetStudiesStudyDto> getStudies(User user);

    List<GetMyStudiesDto> getAttendedStudiesByUser(User user);

    List<TechStack> getTechStacks(Study study);
}
