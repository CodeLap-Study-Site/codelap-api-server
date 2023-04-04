package com.codelap.common.studyNoticeComment.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyTechStack;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.CREATED;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyNoticeCommentTest {

    private User leader;
    private StudyNotice studyNotice;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = User.create("name", 10, career, "abcd", "setUp");

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        List<StudyTechStack> techStackList = Arrays.asList(new StudyTechStack(Spring), new StudyTechStack(Java));

        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
        StudyNoticeFile file = StudyNoticeFile.create("savedName", "originalName", 100L);

        studyNotice = StudyNotice.create(study, "title", "contents", List.of(file));
    }

    @Test
    void 스터디_공지_댓글_생성_성공() {
        StudyNoticeComment studyNoticeComment = StudyNoticeComment.create(studyNotice, leader, "content");

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
}