package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.study.dto.GetMyStudiesDto.GetStudyInfo;
import com.codelap.common.study.dto.GetMyStudiesDto.GetTechStackInfo;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
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

        List<GetStudyInfo> allStudies = studyQueryAppService.getAttendedStudiesByUser(user);

        Map<Long, List<GetTechStackInfo>> techStacksMap = studyQueryAppService.getTechStacks(toStudyIds(allStudies))
                .stream()
                .collect(Collectors
                        .groupingBy(GetTechStackInfo::getStudyId));

        allStudies.forEach(study -> study.setTechStackList(techStacksMap.get(study.getStudyId())));

        return getGetStudiesStudyDto(allStudies);
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
        return studyQueryAppService.getOpenedStudies();
    }
}
