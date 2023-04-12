package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetMyStudiesDto;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

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
    public List<GetMyStudiesDto> getAllStudies(User user) {
        List<GetMyStudiesDto> allStudies = studyQueryAppService.getAllStudies(user);

        IntStream.range(0, user.getStudies().size())
                .forEach(index -> {
                    allStudies.get(index).setTechStackList(studyQueryAppService.findTechStack(user.getStudies().get(index)));
                });

        return allStudies;
    }
}
