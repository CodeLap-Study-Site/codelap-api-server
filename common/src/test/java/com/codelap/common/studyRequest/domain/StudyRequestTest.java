package com.codelap.common.studyRequest.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import com.codelap.fixture.StudyRequestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.codelap.common.studyRequest.domain.StudyRequestStatus.*;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyRequestFixture.createStudyRequest;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class StudyRequestTest {

    private User leader;
    private User user;
    private Study study;
    private StudyRequest studyRequest;

    @BeforeEach
    void setUp() {
        leader = createActivateUser("leader");
        user = createActivateUser("user");
        study = createStudy(leader);
        studyRequest = createStudyRequest(study, user);
    }

    @Test
    void 스터디_참가_신청_성공() {
        StudyRequest studyRequest = createStudyRequest(study, user);

        assertThat(studyRequest.getUser()).isEqualTo(user);
        assertThat(studyRequest.getStudy()).isEqualTo(study);
        assertThat(studyRequest.getMessage()).isEqualTo("message");
        assertThat(studyRequest.getStatus()).isEqualTo(REQUESTED);
        assertThat(studyRequest.getCreatedAt()).isNotNull();
    }

    @Test
    void 스터디_참가_신청_실패__회원이_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyRequest.create(null, study, "참여신청"));
    }

    @Test
    void 스터디_참가_신청_실패__스터디가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyRequest.create(user, null, "참여신청"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_참가_신청_실패__메세지가_공백_혹은_널(String message) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyRequest.create(user, study, message));
    }

    @Test
    void 스터디_참가_신청_실패__이미_있는_회원() {
        study.addMember(user);

        assertThatIllegalArgumentException().isThrownBy(() -> StudyRequest.create(user, study, "message"));
    }

    @Test
    void 스터디_참가_신청_수락_성공() {
        studyRequest.approve();

        assertThat(studyRequest.getStatus()).isEqualTo(APPROVED);
    }

    @ParameterizedTest
    @EnumSource(value = StudyRequestStatus.class, names = {"REQUESTED"}, mode = EXCLUDE)
    void 스터디_참가_신청_수락_실패__수락_가능한_상태가_아님(StudyRequestStatus status) {
        studyRequest.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyRequest.approve());
    }

    @Test
    void 스터디_참가_신청_거절_성공() {
        studyRequest.reject("rejectMessage");

        assertThat(studyRequest.getStatus()).isEqualTo(REJECTED);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_참가_신청_거절_실패__거절_메세지가_널이거나_공백(String messeage) {
        assertThatIllegalArgumentException().isThrownBy(() -> studyRequest.reject(messeage));
    }

    @ParameterizedTest
    @EnumSource(value = StudyRequestStatus.class, names = {"REQUESTED"}, mode = EXCLUDE)
    void 스터디_참가_신청_거절_실패__요청됨_상태가_아님(StudyRequestStatus status) {
        studyRequest.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyRequest.reject("rejectMessage"));
    }

    @Test
    void 스터디_참가_신청_취소_성공() {
        studyRequest.cancel();

        assertThat(studyRequest.getStatus()).isEqualTo(CANCELED);
    }

    @ParameterizedTest
    @EnumSource(value = StudyRequestStatus.class, names = {"REQUESTED"}, mode = EXCLUDE)
    void 스터디_참가_신청_취소_실패__취소_가능한_상태가_아님(StudyRequestStatus status) {
        studyRequest.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyRequest.cancel());
    }
}