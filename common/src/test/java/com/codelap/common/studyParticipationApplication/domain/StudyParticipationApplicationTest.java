package com.codelap.common.studyParticipationApplication.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;

import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.studyParticipationApplication.domain.ApplicationStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StudyParticipationApplicationTest {

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

        study = create("팀", "설명", 4, NORMAL, period, needCareer, leader);
    }

    @Test
    void 스터디_참가_신청_성공(){
        User user = User.create("candidate", 10, career, "abcd", "email");
        StudyParticipationApplication application = StudyParticipationApplication.create(user, study, "참여신청", REQUESTED);

        assertThat(application.getUser()).isEqualTo(user);
        assertThat(application.getStudy()).isEqualTo(study);
        assertThat(application.getMessage()).isEqualTo("참여신청");

        assertThat(application.getApplicationStatus()).isEqualTo(REQUESTED);

    }

    @Test
    void 스터디_참가_신청_실패__회원이_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyParticipationApplication.create(null, study, "참여신청", REQUESTED));
    }

    @Test
    void 스터디_참가_신청_실패__스터디가_널() {
        User user = User.create("candidate", 10, career, "abcd", "email");

        assertThatIllegalArgumentException().isThrownBy(() -> StudyParticipationApplication.create(user, null, "참여신청", REQUESTED));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_참가_신청_실패__메세지가_공백_혹은_널(String message) {
        User user = User.create("candidate", 10, career, "abcd", "email");

        assertThatIllegalArgumentException().isThrownBy(() -> StudyParticipationApplication.create(user, study, message, REQUESTED));
    }

    @Test
    void 스터디_참가_신청_실패__요청됨_상태가_아님() {
        User user = User.create("candidate", 10, career, "abcd", "email");

        StudyParticipationApplication studyParticipationApplication = new StudyParticipationApplication();
        studyParticipationApplication.setApplicationStatus(DELETED);

        assertThatIllegalStateException().isThrownBy(() -> StudyParticipationApplication.create(user, study, "참여신청", null));
    }
}