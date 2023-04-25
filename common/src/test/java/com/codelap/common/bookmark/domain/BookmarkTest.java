package com.codelap.common.bookmark.domain;

import com.codelap.common.study.domain.*;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
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
        List<TechStack> techStackList = Arrays.asList(Java, Spring);

        study = Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
    }

    @Test
    void 북마크_생성_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        com.codelap.common.bookmark.domain.Bookmark bookmark = com.codelap.common.bookmark.domain.Bookmark.create(study, user);

        assertThat(bookmark.getCreatedAt()).isNotNull();
    }

    @Test
    void 북마크_생성_실패__리더는_금지() {
        assertThatIllegalArgumentException().isThrownBy(() -> com.codelap.common.bookmark.domain.Bookmark.create(study, leader));
    }

    @Test
    void 북마크_생성_실패__유저가_삭제된_상태() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        user.setStatus(UserStatus.DELETED);

        assertThatIllegalStateException().isThrownBy(() -> com.codelap.common.bookmark.domain.Bookmark.create(study, user));
    }

    @Test
    void 북마크_생성_실패__스터디가_삭제된_상태() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        study.setStatus(StudyStatus.DELETED);

        assertThatIllegalStateException().isThrownBy(() -> com.codelap.common.bookmark.domain.Bookmark.create(study, user));
    }

    @Test
    void 북마크_삭제_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        com.codelap.common.bookmark.domain.Bookmark bookmark = com.codelap.common.bookmark.domain.Bookmark.create(study, user);

        bookmark.delete(study, user);

        assertThat(!study.containsBookmark(bookmark)).isTrue();
    }

    @Test
    void 북마크_삭제_실패__리더는_금지() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        com.codelap.common.bookmark.domain.Bookmark bookmark = com.codelap.common.bookmark.domain.Bookmark.create(study, user);

        assertThatIllegalArgumentException().isThrownBy(() -> bookmark.delete(study, leader));
    }

    @Test
    void 북마크_삭제_실패__스터디가_삭제된_상태() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        com.codelap.common.bookmark.domain.Bookmark bookmark = com.codelap.common.bookmark.domain.Bookmark.create(study, user);

        study.setStatus(StudyStatus.DELETED);

        assertThatIllegalStateException().isThrownBy(() -> bookmark.delete(study, user));
    }

    @Test
    void 북마크_삭제_실패__유저가_삭제된_상태() {
        UserCareer career = UserCareer.create("직무", 1);
        User user = User.create("name", 10, career, "abcd", "name");

        com.codelap.common.bookmark.domain.Bookmark bookmark = com.codelap.common.bookmark.domain.Bookmark.create(study, user);

        user.setStatus(UserStatus.DELETED);

        assertThatIllegalStateException().isThrownBy(() -> bookmark.delete(study, user));
    }
}