package com.codelap.common.studyRequest.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;

import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyRequestTest {

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
    void 스터디_참가_신청_성공() {
        User user = User.create("candidate", 10, career, "abcd", "email");
        StudyRequest application = StudyRequest.create(user, study, "참여신청");

        assertThat(application.getUser()).isEqualTo(user);
        assertThat(application.getStudy()).isEqualTo(study);
        assertThat(application.getMessage()).isEqualTo("참여신청");
    }

    @Test
    void 스터디_참가_신청_실패__회원이_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyRequest.create(null, study, "참여신청"));
    }

    @Test
    void 스터디_참가_신청_실패__스터디가_널() {
        User user = User.create("candidate", 10, career, "abcd", "email");

        assertThatIllegalArgumentException().isThrownBy(() -> StudyRequest.create(user, null, "참여신청"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_참가_신청_실패__메세지가_공백_혹은_널(String message) {
        User user = User.create("candidate", 10, career, "abcd", "email");

        assertThatIllegalArgumentException().isThrownBy(() -> StudyRequest.create(user, study, message));
    }
}