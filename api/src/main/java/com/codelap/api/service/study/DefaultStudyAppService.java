package com.codelap.api.service.study;


import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyFile;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.dto.GetOpenedStudiesDto;
import com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import com.codelap.common.study.dto.GetStudiesCardDto.GetTechStackInfo;
import com.codelap.common.support.Preconditions;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.integration.s3.FileUpload;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.codelap.common.study.domain.StudyFile.*;


@Service
@Transactional
@RequiredArgsConstructor
public class DefaultStudyAppService implements StudyAppService {

    private final StudyQueryAppService studyQueryDslAppService;
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FileUpload fileUpload;

    @Override
    public List<GetStudiesStudyDto> getStudies(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return studyQueryDslAppService.getStudies(user);
    }

    @Override
    public List<GetStudyInfo> getAttendedStudiesByUser(Long userId, String statusCond, List<TechStack> techStackList) {
        User user = userRepository.findById(userId).orElseThrow();

        return getGetStudyInfo(studyQueryDslAppService.getAttendedStudiesByUser(user, statusCond, techStackList));
    }

    @Override
    public List<GetStudyInfo> getBookmarkedStudiesByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        List<Long> studyIds = bookmarkRepository.findByUser(user)
                .stream()
                .map(DefaultStudyAppService::getId)
                .collect(Collectors.toList());

        return getGetStudyInfo(studyQueryDslAppService.getBookmarkedStudiesByUser(studyIds));
    }

    @Override
    public List<GetOpenedStudiesDto> getOpenedStudies() {
        return studyQueryDslAppService.getOpenedStudies();
    }

    @Override
    public void imageUpload(Long leaderId, Long studyId, MultipartFile multipartFile) throws IOException {
        User leader = userRepository.findById(leaderId).orElseThrow();
        Study study = studyRepository.findById(studyId).orElseThrow();

        Preconditions.actorValidate(study.isLeader(leader));

        study.changeImage(List.of((StudyFile) fileUpload.upload(multipartFile, dirName, create())));
    }

    private static Long getId(Bookmark bookmark) {
        return bookmark.getStudy().getId();
    }

    private List<Long> toStudyIds(List<GetStudyInfo> allStudies) {
        return allStudies
                .stream()
                .map(GetStudyInfo::getStudyId)
                .collect(Collectors.toList());
    }

    private List<GetStudyInfo> getGetStudyInfo(List<GetStudyInfo> allStudies) {
        Map<Long, List<GetTechStackInfo>> techStacksMap = studyQueryDslAppService.getTechStacks(toStudyIds(allStudies))
                .stream()
                .collect(Collectors
                        .groupingBy(GetTechStackInfo::getStudyId));

        allStudies.forEach(study -> study.setTechStackList(techStacksMap.get(study.getStudyId())));

        return allStudies;
    }
}
