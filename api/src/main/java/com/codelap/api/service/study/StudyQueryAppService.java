package com.codelap.api.service.study;


import com.codelap.api.controller.study.cond.GetStudyCardsCond;
import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import com.codelap.common.study.dto.GetStudiesCardDto.GetTechStackInfo;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;

import java.util.List;

import static com.codelap.api.controller.study.cond.GetStudyCardsCond.*;

public interface StudyQueryAppService {

    List<GetStudiesStudyDto> getStudies(User user);

    List<GetStudyInfo> getAttendedStudiesByUser(User userCond, String statusCond, List<TechStack> techStackList);

    List<GetStudyInfo> getBookmarkedStudiesByUser(List<Long> studyIds);

    List<GetTechStackInfo> getTechStacks(List<Long> studyIds);

    List<GetOpenedStudiesDto> getOpenedStudies();
}
