package com.codelap.common.studyRequest.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;

import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.studyRequest.domain.StudyRequestStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

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
        StudyRequest studyRequest = StudyRequest.create(user, study, "참여신청");

        assertThat(studyRequest.getUser()).isEqualTo(user);
        assertThat(studyRequest.getStudy()).isEqualTo(study);
        assertThat(studyRequest.getMessage()).isEqualTo("참여신청");
        assertThat(studyRequest.getStatus()).isEqualTo(REQUESTED);
        assertThat(studyRequest.getCreatedAt()).isNotNull();
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

    @Test
    void 스터디_참가_신청_실패__이미_있는_회원() {
        User user = User.create("candidate", 10, career, "abcd", "email");

        study.addMember(user);

        assertThatIllegalArgumentException().isThrownBy(() -> StudyRequest.create(user, study, "message"));
    }

    @Test
    void 스터디_참가_신청_수락_성공() {
        User user = User.create("candidate", 10, career, "abcd", "email");
        StudyRequest studyRequest = StudyRequest.create(user, study, "참여신청");

        studyRequest.approve(leader);

        assertThat(studyRequest.getStatus()).isEqualTo(APPROVED);
    }

    @Test
    void 스터디_참가_신청_수락_실패__승인한_사람이_리더가_아님(){
        User user = User.create("candidate", 10, career, "abcd", "email");
        StudyRequest studyRequest = StudyRequest.create(user, study, "참여신청");

        User fakeLeader = User.create("fakeLeader", 10, career, "abcd", "fakeEmail");

        assertThatIllegalArgumentException().isThrownBy(() -> studyRequest.approve(fakeLeader));
    }

    @ParameterizedTest
    @EnumSource(value = StudyRequestStatus.class, names = {"REQUESTED"}, mode = EXCLUDE)
    void 스터디_참가_신청_수락_실패__이미_있는_회원(StudyRequestStatus status) {
        User user = User.create("candidate", 10, career, "abcd", "email");
        StudyRequest studyRequest = StudyRequest.create(user, study, "참여신청");

        study.addMember(user);

        studyRequest.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyRequest.approve(leader));
    }

    @Test
    void 스터디_참가_신청_거절_성공() {
        User user = User.create("candidate", 10, career, "abcd", "email");
        StudyRequest studyRequest = StudyRequest.create(user, study, "참여신청");

        studyRequest.reject("rejectMessage");

        assertThat(studyRequest.getStatus()).isEqualTo(REJECTED);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_참가_신청_거절_실패__거절_메세지가_널이거나_공백(String messeage) {
        User user = User.create("candidate", 10, career, "abcd", "email");
        StudyRequest studyRequest = StudyRequest.create(user, study, "messeage");

        assertThatIllegalArgumentException().isThrownBy(() -> studyRequest.reject(messeage));
    }

    @ParameterizedTest
    @EnumSource(value = StudyRequestStatus.class, names = {"REQUESTED"}, mode = EXCLUDE)
    void 스터디_참가_신청_거절_실패__요청됨_상태가_아님(StudyRequestStatus status) {
        User user = User.create("candidate", 10, career, "abcd", "email");
        StudyRequest studyRequest = StudyRequest.create(user, study, "참여신청");

        studyRequest.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyRequest.reject("rejectMessage"));
    }
}