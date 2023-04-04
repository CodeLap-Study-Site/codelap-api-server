package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetAllStudiesStudyDto;
import com.codelap.common.study.domain.*;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import static com.codelap.common.study.domain.StudyDifficulty.*;
import static com.codelap.common.study.domain.StudyStatus.DELETED;
import static com.codelap.common.support.TechStack.*;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class DefaultStudyAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyAppService studyAppService;

    private User leader;
    private List<StudyTechStack> techStackList;

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<Study> getStudies = studyRepository.findByLeader(leader)
                .stream()
                .filter(it -> it.getStatus() != DELETED)
                .collect(Collectors.toList());

        List<GetStudiesStudyDto> studies = studyAppService.getStudies(leader.getId());

        assertThat(studies.size()).isEqualTo(getStudies.size());
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(new StudyTechStack(Spring), new StudyTechStack(Java));

        for (int i = 0; i < 5; i++) {
            studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList));
        }

        Study study = Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
        study.setStatus(DELETED);

        studyRepository.save(study);
    }

    @Test
    void 모든_스터디_목록_조회_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        모든_스터디_목록_조회_스터디_생성();

        List<Study> studyList = studyRepository.findAll()
                .stream()
                .filter(study -> study.getStatus() != DELETED)
                .collect(Collectors.toList());

        List<GetAllStudiesStudyDto> allStudies = studyAppService.getAllStudies()
                .stream()
                .collect(Collectors.groupingBy(GetAllStudiesStudyDto::getId))
                .values()
                .stream()
                .map(list -> {
                    GetAllStudiesStudyDto dto = list.get(0);
                    dto.setTechStackList(list.stream()
                            .flatMap(s -> s.getTechStackList().stream())
                            .distinct()
                            .collect(Collectors.toList()));
                    return dto;
                })
                .collect(Collectors.toList());

        assertThat(studyList.size()).isEqualTo(allStudies.size());
    }

    private void 모든_스터디_목록_조회_스터디_생성() {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        Study study1 = studyRepository.save(Study.create("팀1", "설명1", 4, EASY, period, needCareer, leader, List.of(new StudyTechStack(Spring), new StudyTechStack(Java))));
        Study study2 = studyRepository.save(Study.create("팀2", "설명2", 5, NORMAL, period, needCareer, leader, List.of(new StudyTechStack(Spring), new StudyTechStack(Java))));
        Study study3 = studyRepository.save(Study.create("팀3", "설명3", 6, HARD, period, needCareer, leader, List.of(new StudyTechStack(React), new StudyTechStack(JavaScript), new StudyTechStack(Python))));

        Study study = studyRepository.save(Study.create("팀4", "설명4", 6, HARD, period, needCareer, leader, List.of(new StudyTechStack(Docker))));

        study.delete();
    }
}