package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto;
import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetMyStudiesDto;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
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
    public List<GetStudiesDto.GetStudiesStudyDto> getAttendedStudiesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<GetMyStudiesDto> allStudies = studyQueryAppService.getAttendedStudiesByUser(user);

        IntStream.range(0, user.getStudies().size())
                .forEach(index -> {
                    allStudies.get(index).setTechStackList(studyQueryAppService.getTechStacks(user.getStudies().get(index)));
                });

        return allStudies.stream().map(study -> {
            GetStudiesStudyDto studyDto = new GetStudiesStudyDto(
                    study.getStudyName(),
                    study.getStudyPeriod(),
                    study.getLeaderName(),
                    study.getCommentCount(),
                    study.getViewCount(),
                    study.getBookmarkCount(),
                    study.getMaxMemberSize(),
                    study.getTechStackList()
            );
            return studyDto;
        }).collect(Collectors.toList());
    }
}
