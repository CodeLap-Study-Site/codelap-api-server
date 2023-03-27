package com.codelap.common.bookmark.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyStatus;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static org.assertj.core.api.Assertions.*;

class BookmarkTest {

    private User leader;
    private Study study;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = User.create("name", 10, career, "abcd", "setUp");

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        study = Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader);
    }

    @Test
    void 북마크_생성_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        Bookmark bookmark = Bookmark.create(study, user);

        assertThat(bookmark.getCreatedAt()).isNotNull();
    }

    @Test
    void 북마크_생성_실패__리더는_금지() {
        assertThatIllegalArgumentException().isThrownBy(() -> Bookmark.create(study, leader));
    }

    @Test
    void 북마크_생성_실패__유저가_삭제된_상태() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        user.setStatus(UserStatus.DELETED);

        assertThatIllegalStateException().isThrownBy(() -> Bookmark.create(study, user));
    }

    @Test
    void 북마크_생성_실패__스터디가_삭제된_상태() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        study.setStatus(StudyStatus.DELETED);

        assertThatIllegalStateException().isThrownBy(() -> Bookmark.create(study, user));
    }
}