package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetMyStudiesDto;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.user.domain.User;

import java.util.List;

public interface StudyQueryAppService {

    List<GetStudiesStudyDto> getStudies(User user);

    List<GetMyStudiesDto.GetStudyInfo> getAttendedStudiesByUser(User user);

    List<GetMyStudiesDto.GetTechStackInfo> getTechStacks(List<Long> studyIds);

    List<GetOpenedStudiesDto> getOpenedStudies();
}
