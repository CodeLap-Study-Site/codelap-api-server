package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultStudyQueryAppService implements StudyQueryAppService {

    private final StudyRepository studyRepository;

    @Override
    public List<GetStudiesStudyDto> getStudies(User user) {
        return null;
    }

    @Override
    public List<GetStudiesCardDto.GetStudyInfo> getAttendedStudiesByUser(User userCond, String statusCond, List<TechStack> techStackList) {
        return studyRepository.getAttendedStudiesByUser(userCond);
    }

    @Override
    public List<GetStudiesCardDto.GetTechStackInfo> getTechStacks(List<Long> studyIds) {
        return studyRepository.getTechStacks(studyIds);
    }

    @Override
    public List<GetOpenedStudiesDto> getOpenedStudies() {
        return null;
    }
}
