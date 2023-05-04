package com.codelap.common.studyView.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyViewTest {

    private User leader;
    private Study study;

    @BeforeEach
    void setUp() {
        leader = createActivateUser("leader");
        study = createStudy(leader);
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