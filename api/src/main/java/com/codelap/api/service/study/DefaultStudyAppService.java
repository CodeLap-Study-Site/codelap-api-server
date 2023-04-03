package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetAllStudiesStudyDto;
import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultStudyAppService implements StudyAppService {

    private final StudyQueryAppService studyQueryAppService;
    private final UserRepository userRepository;

    @Override
    public List<GetStudiesStudyDto> getStudies(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return studyQueryAppService.getStudies(user);
    }

    @Override
    public List<GetAllStudiesStudyDto> getAllStudies() {
        return studyQueryAppService.getAllStudies();
    }
}
