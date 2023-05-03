package com.codelap.fixture;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;

import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createUser;

public class BookmarkFixture {
    public static Bookmark createBookmark() {
        return Bookmark.create(createStudy(), createUser());
    }

    public static Bookmark createBookmark(Study study, User user) {
        return Bookmark.create(study, user);
    }
}
