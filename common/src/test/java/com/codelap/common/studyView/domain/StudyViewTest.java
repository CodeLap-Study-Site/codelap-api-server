package com.codelap.common.studyView.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyTechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyViewTest {

    private User leader;
    private Study study;
    private UserCareer career;
    private StudyPeriod period;
    private StudyNeedCareer needCareer;

    @BeforeEach
    void setUp() {
        career = UserCareer.create("직무", 1);
        leader = User.create("name", 10, career, "abcd", "setUp");

        period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        needCareer = StudyNeedCareer.create("직무", 1);

        List<StudyTechStack> techStackList = Arrays.asList(new StudyTechStack(Spring), new StudyTechStack(Java));

        study = create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
    }

    @Test
    void 스터디_조회수_생성_성공() {
        StudyView studyView = StudyView.create(study, "1.1.1.1");

        Assertions.assertThat(studyView.getStudy()).isNotNull();
        Assertions.assertThat(studyView.getIpAddress()).isEqualTo("1.1.1.1");
    }

    @Test
    void 스터디_조회수_생성_실패__스터디가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyView.create(null, "1.1.1.1"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_조회수_생성_실패__IP가_공백_혹은_널(String ipAddress) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyView.create(study, ipAddress));
    }
}