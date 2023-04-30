package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.domain.TechStack;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import com.codelap.common.study.dto.GetStudiesCardDto.GetTechStackInfo;
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

    private final StudyQueryAppService studyQueryDslAppService;
    private final UserRepository userRepository;

    @Override
    public List<GetStudiesStudyDto> getStudies(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return studyQueryDslAppService.getStudies(user);
    }

    @Override
    public List<GetStudyInfo> getAttendedStudiesByUser(Long userId, String statusCond, List<TechStack> techStackList) {
        User user = userRepository.findById(userId).orElseThrow();

        List<GetStudyInfo> allStudies = studyQueryDslAppService.getAttendedStudiesByUser(user, statusCond, techStackList);

        Map<Long, List<GetTechStackInfo>> techStacksMap = studyQueryDslAppService.getTechStacks(toStudyIds(allStudies))
                .stream()
                .collect(Collectors
                        .groupingBy(GetTechStackInfo::getStudyId));

        allStudies.forEach(study -> study.setTechStackList(techStacksMap.get(study.getStudyId())));

        return allStudies;
    }

    private static List<GetStudiesStudyDto> getGetStudiesStudyDto(List<GetStudyInfo> allStudies) {
        return allStudies
                .stream()
                .map(GetStudiesStudyDto::new)
                .collect(Collectors.toList());
    }

    private static List<Long> toStudyIds(List<GetStudyInfo> allStudies) {
        return allStudies
                .stream()
                .map(GetStudyInfo::getStudyId)
                .collect(Collectors.toList());
    }

    @Override
    public List<GetOpenedStudiesDto> getOpenedStudies() {
        return studyQueryDslAppService.getOpenedStudies();
    }
}
