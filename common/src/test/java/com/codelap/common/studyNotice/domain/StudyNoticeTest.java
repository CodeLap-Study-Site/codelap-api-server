package com.codelap.common.studyNotice.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.TechStack;
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
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyNoticeTest {

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

        List<TechStack> techStackList = Arrays.asList(Java, Spring);

        study = create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
    }

    @Test
    void 스터디_공지_생성_성공() {
        StudyNoticeFile file = new StudyNoticeFile("savedName", "originalName", 100L);
        StudyNotice studyNotice = StudyNotice.create(study, "title", "contents", List.of(file));

        assertThat(studyNotice.getTitle()).isEqualTo("title");
        assertThat(studyNotice.getContents()).isEqualTo("contents");
        assertThat(studyNotice.getFiles()).isEqualTo(List.of(file));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_생성_실패__제목이_널이거나_공백(String title) {
        StudyNoticeFile file = new StudyNoticeFile("savedName", "originalName", 100L);

        assertThatIllegalArgumentException().isThrownBy(() -> StudyNotice.create(study, title, "contents", List.of(file)));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_생성_실패__메시지가_널이거나_공백_파일이_널(String contents) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyNotice.create(study, "title", contents, null));
    }
}