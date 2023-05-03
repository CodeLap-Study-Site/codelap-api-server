package com.codelap.fixture;

import com.codelap.common.study.domain.Study;
import com.codelap.common.studyComment.domain.StudyComment;
import com.codelap.common.user.domain.User;

import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createUser;

public class StudyCommentFixture {
    public static StudyComment createStudyComment() {
        return StudyComment.create(createStudy(), createUser(), "comment");
    }

    public static StudyComment createStudyComment(Study study, User user) {
        return StudyComment.create(study, user, "comment");
    }
}
