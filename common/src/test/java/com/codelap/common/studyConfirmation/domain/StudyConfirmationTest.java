package com.codelap.common.studyConfirmation.domain;

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
import java.util.List;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.studyConfirmation.domain.StudyConfirmation.create;
import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class StudyConfirmationTest {

    private User leader;
    private User member;
    private Study study;
    private StudyConfirmationFile file;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = User.create("name", 10, career, "abcd", "setUp");

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        study = Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader);

        member = User.create("name", 10, career, "abcd", "user");

        study.addMember(member);

        file = StudyConfirmationFile.create("saved", "original", 10L);
    }

    @Test
    void 스터디_인증_생성_성공() {
        StudyConfirmation studyConfirmation = create(study, member, "title", "content", List.of(file));

        assertThat(studyConfirmation.getStudy()).isSameAs(study);
        assertThat(studyConfirmation.getUser()).isSameAs(member);
        assertThat(studyConfirmation.getTitle()).isEqualTo("title");
        assertThat(studyConfirmation.getContent()).isEqualTo("content");
        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
        assertThat(studyConfirmation.getCreatedAt()).isNotNull();
    }

    @Test
    void 스터디_인증_생성_실패__스터디가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create(null, member, "title", "content", List.of(file)));
    }

    @Test
    void 스터디_인증_생성_실패__맴버가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create(study, null, "title", "content", List.of(file)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__타이틀이_공백_혹은_널(String title) {
        assertThatIllegalArgumentException().isThrownBy(() -> create(study, member, title, "content", List.of(file)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__컨텐츠가_공백_혹은_널(String content) {
        assertThatIllegalArgumentException().isThrownBy(() -> create(study, member, "title", content, List.of(file)));
    }

    @Test
    void 스터디_인증_생성_실패__파일이_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> create(study, member, "title", "content", null));
    }

    @Test
    void 스터디_인증_확인_성공() {
        StudyConfirmation studyConfirmation = create(study, member, "title", "content", List.of(file));

        studyConfirmation.confirm();

        assertThat(studyConfirmation.getStatus()).isEqualTo(CONFIRMED);
        assertThat(studyConfirmation.getCheckedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = StudyConfirmationStatus.class, names = {"CREATED"}, mode = EXCLUDE)
    void 스터디_인증_확인_실패__확인_가능한_상태가_아님(StudyConfirmationStatus status) {
        StudyConfirmation studyConfirmation = create(study, member, "title", "content", List.of(file));

        studyConfirmation.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyConfirmation.confirm());
    }

    @Test
    void 스터디_인증_거절_성공() {
        StudyConfirmation studyConfirmation = create(study, member, "title", "content", List.of(file));

        studyConfirmation.reject("부적합");

        assertThat(studyConfirmation.getStatus()).isEqualTo(REJECTED);
        assertThat(studyConfirmation.getRejectedMessage()).isEqualTo("부적합");
        assertThat(studyConfirmation.getCheckedAt()).isNotNull();
    }

    @ParameterizedTest
    @EnumSource(value = StudyConfirmationStatus.class, names = {"CREATED"}, mode = EXCLUDE)
    void 스터디_인증_거절_실패__확인_가능한_상태가_아님(StudyConfirmationStatus status) {
        StudyConfirmation studyConfirmation = create(study, member, "title", "content", List.of(file));

        studyConfirmation.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyConfirmation.reject("부적합"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_거절_실패__거절_메시지가_널이거나_공백(String rejectedMessage) {
        StudyConfirmation studyConfirmation = create(study, member, "title", "content", List.of(file));

        assertThatIllegalArgumentException().isThrownBy(() -> studyConfirmation.reject(rejectedMessage));
    }
}
