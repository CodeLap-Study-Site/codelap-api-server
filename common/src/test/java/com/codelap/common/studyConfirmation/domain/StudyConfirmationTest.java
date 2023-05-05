package com.codelap.common.studyConfirmation.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import com.codelap.fixture.StudyFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static com.codelap.common.studyConfirmation.domain.StudyConfirmation.create;
import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.*;
import static com.codelap.fixture.StudyConfirmationFixture.createStudyConfirmation;
import static com.codelap.fixture.StudyConfirmationFixture.createStudyConfirmationFiles;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class StudyConfirmationTest {

    private User leader;
    private User member;
    private Study study;
    private StudyConfirmation studyConfirmation;

    @BeforeEach
    void setUp() {
        leader = createActivateUser("leader");
        member = createActivateUser("member");

        study = StudyFixture.createStudy(leader);
        study.addMember(member);

        studyConfirmation = createStudyConfirmation(study, member);
    }

    @Test
    void 스터디_인증_생성_성공() {
        studyConfirmation = createStudyConfirmation(study, member);

        assertThat(studyConfirmation.getStudy()).isSameAs(study);
        assertThat(studyConfirmation.getUser()).isSameAs(member);
        assertThat(studyConfirmation.getTitle()).isEqualTo("title");
        assertThat(studyConfirmation.getContent()).isEqualTo("content");
        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
        assertThat(studyConfirmation.getCreatedAt()).isNotNull();
    }

    @Test
    void 스터디_인증_생성_실패__스터디가_널() {
        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> create(null, member, "title", "content", files));
    }

    @Test
    void 스터디_인증_생성_실패__맴버가_널() {
        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> create(study, null, "title", "content", files));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__타이틀이_공백_혹은_널(String title) {
        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> create(study, member, title, "content", files));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__컨텐츠가_공백_혹은_널(String content) {
        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> create(study, member, "title", content, files));
    }

    @Test
    void 스터디_인증_생성_실패__파일이_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create(study, member, "title", "content", null));
    }

    @Test
    void 스터디_인증_확인_성공() {
        studyConfirmation.confirm();

        assertThat(studyConfirmation.getStatus()).isEqualTo(CONFIRMED);
        assertThat(studyConfirmation.getConfirmedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = StudyConfirmationStatus.class, names = {"CREATED"}, mode = EXCLUDE)
    void 스터디_인증_확인_실패__확인_가능한_상태가_아님(StudyConfirmationStatus status) {
        studyConfirmation.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyConfirmation.confirm());
    }

    @Test
    void 스터디_인증_거절_성공() {
        studyConfirmation.reject("부적합");

        assertThat(studyConfirmation.getStatus()).isEqualTo(REJECTED);
        assertThat(studyConfirmation.getRejectedMessage()).isEqualTo("부적합");
        assertThat(studyConfirmation.getRejectedAt()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_거절_실패__거절_메시지가_널이거나_공백(String rejectedMessage) {
        assertThatIllegalArgumentException().isThrownBy(() -> studyConfirmation.reject(rejectedMessage));
    }

    @ParameterizedTest
    @EnumSource(value = StudyConfirmationStatus.class, names = {"CREATED"}, mode = EXCLUDE)
    void 스터디_인증_거절_실패__확인_가능한_상태가_아님(StudyConfirmationStatus status) {
        studyConfirmation.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyConfirmation.reject("부적합"));
    }

    @Test
    void 스터디_인증_재인증_성공() {
        studyConfirmation.setStatus(REJECTED);

        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        studyConfirmation.reConfirm("modifyTitle", "modifyContent", files);

        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_재인증_실패__타이틀이_공백_혹은_널(String modifyTitle) {
        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> studyConfirmation.reConfirm(modifyTitle, "modifyContent", files));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_재인증_실패__컨텐츠가_공백_혹은_널(String modifyContent) {
        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> studyConfirmation.reConfirm("title", modifyContent, files));
    }

    @Test
    void 스터디_인증_재인증_실패__파일이_공백_혹은_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> studyConfirmation.reConfirm("title", "content", null));
    }

    @ParameterizedTest
    @EnumSource(value = StudyConfirmationStatus.class, names = {"REJECTED"}, mode = EXCLUDE)
    void 스터디_인증_재인증_실패__거절된_상태가_아님(StudyConfirmationStatus status) {
        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        studyConfirmation.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyConfirmation.reConfirm("modifyTitle", "modifyContent", files));
    }

    @Test
    void 스터디_인증_삭제_성공() {
        studyConfirmation.delete();

        assertThat(studyConfirmation.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_인증_삭제_실패__이미_삭제된_인증() {
        studyConfirmation.setStatus(DELETED);

        assertThatIllegalStateException().isThrownBy(() -> studyConfirmation.delete());
    }
}
