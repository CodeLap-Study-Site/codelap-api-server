package com.codelap.common.studyNotice.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.DELETED;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyNoticeFixture.createStudyNotice;
import static com.codelap.fixture.StudyNoticeFixture.createStudyNoticeFiles;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

class StudyNoticeTest {

    private User leader;
    private Study study;
    private StudyNotice studyNotice;

    @BeforeEach
    void setUp() {
        leader = createActivateUser("leader");
        study = createStudy(leader);
        studyNotice = createStudyNotice(study);
    }

    @Test
    void 스터디_공지_생성_성공() {
        StudyNotice studyNotice = createStudyNotice(study);

        assertThat(studyNotice.getTitle()).isEqualTo("title");
        assertThat(studyNotice.getContents()).isEqualTo("content");
        assertThat(studyNotice.getFiles()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_생성_실패__제목이_널이거나_공백(String title) {
        List<StudyNoticeFile> files = createStudyNoticeFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> StudyNotice.create(study, title, "contents", files));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_생성_실패__메시지가_널이거나_공백_파일이_널(String contents) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyNotice.create(study, "title", contents, null));
    }

    @Test
    void 스터디_공지_수정_성공() {
        List<StudyNoticeFile> files = createStudyNoticeFiles();

        studyNotice.update("title", "contents", files);

        assertThat(studyNotice.getTitle()).isEqualTo("title");
        assertThat(studyNotice.getContents()).isEqualTo("contents");
        assertThat(studyNotice.getFiles()).isEqualTo(files);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_수정_실패__제목이_널이거나_공백(String title) {
        List<StudyNoticeFile> files = createStudyNoticeFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> studyNotice.update(title, "contents", files));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_수정_실패__내용이_널이거나_공백(String contents) {
        List<StudyNoticeFile> files = createStudyNoticeFiles();

        assertThatIllegalArgumentException().isThrownBy(() -> studyNotice.update("title", contents, files));
    }

    @Test
    void 스터디_공지_삭제_성공() {
        studyNotice.delete();

        assertThat(studyNotice.getStatus()).isEqualTo(DELETED);
    }

    @ParameterizedTest
    @EnumSource(value = StudyNoticeStatus.class, names = {"DELETED"}, mode = INCLUDE)
    void 스터디_공지_삭제_실패__이미_삭제된_상태(StudyNoticeStatus status) {
        studyNotice.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyNotice.delete());
    }
}