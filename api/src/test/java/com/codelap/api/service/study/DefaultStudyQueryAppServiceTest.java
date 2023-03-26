package com.codelap.api.service.study;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.DELETED;
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

    private User leader;


    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));
    }

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        유저가_참여한_스터디_조회_스터디_생성(leader);

        유저가_참여한_스터디_조회_검증(leader);
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        for (int i = 0; i < 5; i++) {
            studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));
        }

        Study study = Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader);
        study.setStatus(DELETED);

        studyRepository.save(study);
    }

    private void 유저가_참여한_스터디_조회_검증(User leader) {
        List<GetStudiesStudyDto> getStudies = studyRepository.findByLeader(leader)
                .stream()
                .filter(it -> it.getStatus() != DELETED)
                .map(study -> new GetStudiesStudyDto(study.getId(), study.getName(), study.getCreatedAt(), study.getStatus()))
                .collect(Collectors.toList());

        List<GetStudiesStudyDto> studies = studyQueryAppService.getStudies(leader);

        IntStream.range(0, getStudies.size())
                .forEach(index -> {
                    assertThat(studies.get(index).id()).isEqualTo(getStudies.get(index).id());
                    assertThat(studies.get(index).name()).isEqualTo(getStudies.get(index).name());
                    assertThat(studies.get(index).createdAt()).isNotNull();
                    assertThat(getStudies.get(index).createdAt()).isNotNull();
                    assertThat(studies.get(index).status()).isEqualTo(getStudies.get(index).status());
                });
    }
}