package com.codelap.fixture;

import com.codelap.common.study.domain.Study;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.user.domain.User;

import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createUser;

public class StudyRequestFixture {
    public static StudyRequest createStudyRequest() {
        return StudyRequest.create(createUser(), createStudy(), "message");
    }

    public static StudyRequest createStudyRequest(Study study, User user) {
        return StudyRequest.create(user, study, "message");
    }
}
