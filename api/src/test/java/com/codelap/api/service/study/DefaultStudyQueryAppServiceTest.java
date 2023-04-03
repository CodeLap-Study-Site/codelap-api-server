package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetAllStudiesStudyDto;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyComment.domain.StudyComment;
import com.codelap.common.studyComment.domain.StudyCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import static com.codelap.common.study.domain.StudyDifficulty.*;
import static com.codelap.common.study.domain.StudyStatus.DELETED;
import static com.codelap.common.study.domain.TechStack.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DefaultStudyQueryAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyQueryAppService studyQueryAppService;

    @Autowired
    StudyCommentRepository studyCommentRepository;

    private User leader;
    private List<TechStack> techStackList;

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<Study> getStudies = studyRepository.findByLeader(leader)
                .stream()
                .filter(it -> it.getStatus() != DELETED)
                .collect(Collectors.toList());

        List<GetStudiesStudyDto> studies = studyQueryAppService.getStudies(leader);

        IntStream.range(0, getStudies.size())
                .forEach(index -> {
                    assertThat(studies.get(index).id()).isEqualTo(getStudies.get(index).getId());
                    assertThat(studies.get(index).name()).isEqualTo(getStudies.get(index).getName());
                    assertThat(studies.get(index).createdAt()).isNotNull();
                    assertThat(getStudies.get(index).getCreatedAt()).isNotNull();
                    assertThat(studies.get(index).status()).isEqualTo(getStudies.get(index).getStatus());
                });
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(Java, Spring);

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

        List<GetAllStudiesStudyDto> allStudies = studyQueryAppService.getAllStudies();

        List<GetAllStudiesStudyDto> uniqueStudies = allStudies.stream()
                .filter(distinctByKey(GetAllStudiesStudyDto::getId))
                .collect(Collectors.toList());

        IntStream.range(0, uniqueStudies.size())
                .forEach(i -> {
                    assertThat(uniqueStudies.get(i).getLeader()).isEqualTo(studyList.get(i).getLeader().getName());
                    assertThat(uniqueStudies.get(i).getName()).isEqualTo(studyList.get(i).getName());
                    assertThat(uniqueStudies.get(i).getId()).isEqualTo(studyList.get(i).getId());
                });

        System.out.println(allStudies);

    }

    private void 모든_스터디_목록_조회_스터디_생성() {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        Study study1 = studyRepository.save(Study.create("팀1", "설명1", 4, EASY, period, needCareer, leader, List.of(Java, Spring)));
        Study study2 = studyRepository.save(Study.create("팀2", "설명2", 5, NORMAL, period, needCareer, leader, List.of(Figma, AWS)));
        Study study3 = studyRepository.save(Study.create("팀3", "설명3", 6, HARD, period, needCareer, leader, List.of(React, JavaScript)));

        Study study = studyRepository.save(Study.create("팀4", "설명4", 6, HARD, period, needCareer, leader, List.of(Docker)));

        study.delete();

        studyCommentRepository.save(StudyComment.create(study1, leader, "comment"));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}