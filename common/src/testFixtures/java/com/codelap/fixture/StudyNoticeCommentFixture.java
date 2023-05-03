package com.codelap.fixture;

import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;
import com.codelap.common.user.domain.User;

import static com.codelap.fixture.StudyNoticeFixture.createStudyNotice;
import static com.codelap.fixture.UserFixture.createUser;

public class StudyNoticeCommentFixture {
    public static StudyNoticeComment createStudyNoticeComment() {
        return StudyNoticeComment.create(createStudyNotice(), createUser(), "content");
    }

    public static StudyNoticeComment createStudyNoticeComment(StudyNotice studyNotice, User user) {
        return StudyNoticeComment.create(studyNotice, user, "content");
    }
}
