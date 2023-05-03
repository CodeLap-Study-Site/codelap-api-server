package com.codelap.common.bookmark.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyStatus;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codelap.common.bookmark.domain.Bookmark.create;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createUser;
import static org.assertj.core.api.Assertions.*;

class BookmarkTest {

    private User leader;
    private Study study;

    @BeforeEach
    void setUp() {
        leader = createUser("leader");
        study = createStudy(leader);
    }

    @Test
    void 북마크_생성_성공() {
        User user = createUser();

        Bookmark bookmark = create(study, user);

        assertThat(bookmark.getCreatedAt()).isNotNull();
    }

    @Test
    void 북마크_생성_실패__리더는_금지() {
        assertThatIllegalArgumentException().isThrownBy(() -> create(study, leader));
    }

    @Test
    void 북마크_생성_실패__유저가_삭제된_상태() {
        User user = createUser();

        user.setStatus(UserStatus.DELETED);

        assertThatIllegalStateException().isThrownBy(() -> create(study, user));
    }

    @Test
    void 북마크_생성_실패__스터디가_삭제된_상태() {
        User user = createUser();

        study.setStatus(StudyStatus.DELETED);

        assertThatIllegalStateException().isThrownBy(() -> create(study, user));
    }
}
