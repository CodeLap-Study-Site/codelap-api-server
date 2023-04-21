package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetMyStudiesDto;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetTechStackDto;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    public List<GetStudiesStudyDto> getAttendedStudiesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<GetMyStudiesDto> allStudies = studyQueryAppService.getAttendedStudiesByUser(user);

        Map<Long, List<GetTechStackDto>> techStacksMap = studyQueryAppService.getTechStacks(toStudyIds(allStudies))
                .stream()
                .collect(Collectors.groupingBy(GetTechStackDto::getStudyId));

        allStudies.forEach(it -> it.setTechStackList(techStacksMap.get(it.getStudyId())));

        return getGetStudiesStudyDto(allStudies);
    }

    private static List<GetStudiesStudyDto> getGetStudiesStudyDto(List<GetMyStudiesDto> allStudies) {
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

    private static List<Long> toStudyIds(List<GetMyStudiesDto> allStudies) {
        return allStudies.stream().map(study -> study.getStudyId()).collect(Collectors.toList());
    }

    @Override
    public List<GetOpenedStudiesDto> getOpenedStudies() {
        return studyQueryAppService.getOpenedStudies();
    }
}
