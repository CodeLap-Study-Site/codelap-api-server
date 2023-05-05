package com.codelap.common.studyNoticeComment.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.CREATED;
import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.DELETED;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyNoticeCommentFixture.createStudyNoticeComment;
import static com.codelap.fixture.StudyNoticeFixture.createStudyNotice;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

class StudyNoticeCommentTest {

    private User leader;
    private Study study;
    private StudyNotice studyNotice;

    private StudyNoticeComment studyNoticeComment;

    @BeforeEach
    void setUp() {
        leader = createActivateUser("leader");
        study = createStudy(leader);
        studyNotice = createStudyNotice(study);
        studyNoticeComment = createStudyNoticeComment(studyNotice, leader);
    }

    @Test
    void 스터디_공지_댓글_생성_성공() {
        StudyNoticeComment studyNoticeComment = createStudyNoticeComment(studyNotice, leader);

        assertThat(studyNoticeComment.getContent()).isEqualTo("content");
        assertThat(studyNoticeComment.getUser()).isSameAs(leader);
        assertThat(studyNoticeComment.getCreateAt()).isNotNull();
        assertThat(studyNoticeComment.getStatus()).isEqualTo(CREATED);
    }

    @Test
    void 스터디_공지_댓글_생성_실패__스터디공지가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyNoticeComment.create(null, leader, "content"));
    }

    @Test
    void 스터디_공지_댓글_생성_실패__유저가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyNoticeComment.create(studyNotice, null, "content"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_댓글_생성_실패__댓글내용이_널이거나_공백(String content) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyNoticeComment.create(studyNotice, leader, content));
    }

    @Test
    void 스터디_공지_댓글_삭제_성공() {
        studyNoticeComment.delete();

        assertThat(studyNoticeComment.getStatus()).isEqualTo(DELETED);
    }

    @ParameterizedTest
    @EnumSource(value = StudyNoticeCommentStatus.class, names = {"DELETED"}, mode = INCLUDE)
    void 스터디_공지_댓글_삭제_실패__이미_삭제된_상태(StudyNoticeCommentStatus status) {
        studyNoticeComment.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyNoticeComment.delete());
    }

    @Test
    void 스터디_공지_댓글_수정_성공() {
        studyNoticeComment.update("content");

        assertThat(studyNoticeComment.getContent()).isEqualTo("content");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_댓글_수정_실패__댓글내용이_널이거나_공백(String content) {
        assertThatIllegalArgumentException().isThrownBy(() -> studyNoticeComment.update(content));
    }
}