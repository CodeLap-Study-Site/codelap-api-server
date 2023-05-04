package com.codelap.common.study.domain;

import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class StudyRepositoryTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    private User leader;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
    }

    @Test
    void 스터디_생성_성공() {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        List<StudyTechStack> techStackList = Arrays.asList(new StudyTechStack(Java), new StudyTechStack(Spring));

        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);

        studyRepository.save(study);

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getId()).isNotNull();
        assertThat(foundStudy.getName()).isEqualTo("팀");
        assertThat(foundStudy.getInfo()).isEqualTo("설명");
        assertThat(foundStudy.getMaxMembersSize()).isEqualTo(4);
        assertThat(foundStudy.getDifficulty()).isEqualTo(NORMAL);
        assertThat(foundStudy.getPeriod()).isSameAs(period);
        assertThat(foundStudy.getNeedCareer()).isSameAs(needCareer);
        assertThat(foundStudy.getLeader()).isSameAs(leader);
        assertThat(foundStudy.getMembers()).containsExactly(leader);
        assertThat(foundStudy.getStatus()).isEqualTo(OPENED);
        assertThat(foundStudy.getCreatedAt()).isNotNull();
    }
}