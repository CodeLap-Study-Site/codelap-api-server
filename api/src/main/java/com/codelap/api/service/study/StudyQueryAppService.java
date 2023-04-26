package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.user.domain.User;

import java.util.List;

public interface StudyQueryAppService {

    List<GetStudiesStudyDto> getStudies(User user);

    List<GetStudiesCardDto.GetStudyInfo> getAttendedStudiesByUser(User user);

    List<GetStudiesCardDto.GetTechStackInfo> getTechStacks(List<Long> studyIds);

    List<GetOpenedStudiesDto> getOpenedStudies();
}
