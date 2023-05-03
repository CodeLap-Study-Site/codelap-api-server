package com.codelap.common.studyComment.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.codelap.common.studyComment.domain.StudyCommentStatus.CREATED;
import static com.codelap.common.studyComment.domain.StudyCommentStatus.DELETED;
import static com.codelap.fixture.StudyCommentFixture.createStudyComment;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createUser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

class StudyCommentTest {

    private User leader;
    private Study study;
    private StudyComment studyComment;


    @BeforeEach
    void setUp() {
        leader = createUser("leader");
        study = createStudy(leader);
        studyComment = createStudyComment(study, leader);
    }

    @Test
    void 스터디_댓글_생성_성공() {
        StudyComment studyComment = StudyComment.create(study, leader, "댓글");

        assertThat(studyComment.getComment()).isEqualTo("댓글");
        assertThat(studyComment.getUser()).isSameAs(leader);
        assertThat(studyComment.getStatus()).isEqualTo(CREATED);
        assertThat(studyComment.getCreateAt()).isNotNull();
    }

    @Test
    void 스터디_댓글_생성_실패__스터디가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyComment.create(null, leader, "댓글"));
    }

    @Test
    void 스터디_댓글_생성_실패__유저가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyComment.create(study, null, "댓글"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_댓글_생성_실패__댓글이_널이거나_공백(String comment) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyComment.create(study, leader, comment));
    }

    @Test
    void 스터디_댓글_수정_성공() {
        studyComment.update("updatedComment");

        assertThat(studyComment.getComment()).isEqualTo("updatedComment");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_댓글_수정_실패__댓글이_널이거나_공백(String comment) {
        assertThatIllegalArgumentException().isThrownBy(() -> studyComment.update(comment));
    }

    @Test
    void 스터디_댓글_삭제_성공() {
        studyComment.delete();

        assertThat(studyComment.getStatus()).isEqualTo(DELETED);
    }

    @ParameterizedTest
    @EnumSource(value = StudyCommentStatus.class, names = {"DELETED"}, mode = INCLUDE)
    void 스터디_댓글_삭제_실패__이미_삭제된_상태(StudyCommentStatus status) {
        studyComment.setStatus(status);

        assertThatIllegalStateException().isThrownBy(() -> studyComment.delete());
    }
}