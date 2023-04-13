package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.domain.TechStack;
import com.codelap.common.study.dto.GetMyStudiesDto;
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
    public List<GetMyStudiesDto> getAllStudies(User user) {
        return studyRepository.findAllStudy(user);
    }

    @Override
    public List<TechStack> findTechStack(Study study) {
        return studyRepository.findTechStack(study);
    }
}
