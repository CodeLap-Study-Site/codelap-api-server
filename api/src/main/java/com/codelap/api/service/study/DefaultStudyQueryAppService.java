package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DefaultStudyQueryAppService implements StudyQueryAppService {
    @Override
    public List<GetStudiesStudyDto> getStudies(User user) {
        return null;
    }
}
