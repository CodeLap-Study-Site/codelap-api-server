package com.codelap.api.service.user;

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

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class DefaultUserAppServiceTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAppService userAppService;

    private User leader;
    private User user;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        Study study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));

        Study study2 = studyRepository.save(Study.create("팀2", "설명2", 4, NORMAL, period, needCareer, leader));

        user = userRepository.save(User.create("member", 10, career, "abcd", "member"));

        Study study3 = studyRepository.save(Study.create("팀2", "설명2", 4, NORMAL, period, needCareer, user));
    }

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        List<Study> studyList1 = userAppService.findStudyListByUserId(user.getId());

        assertThat(studyList1.size()).isEqualTo(1);

        List<Study> studyList2 = userAppService.findStudyListByUserId(leader.getId());

        assertThat(studyList2.size()).isEqualTo(2);
    }

}