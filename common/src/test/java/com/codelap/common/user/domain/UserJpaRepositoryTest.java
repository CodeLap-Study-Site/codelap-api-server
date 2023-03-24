package com.codelap.common.user.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
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
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

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
        List<Study> userAttendStudyList = userJpaRepository.findStudyListByUserId(user);

        assertThat(userAttendStudyList.size()).isEqualTo(1);

        List<Study> leaderAttendStudyList = userJpaRepository.findStudyListByUserId(leader);

        assertThat(leaderAttendStudyList.size()).isEqualTo(2);
    }
}