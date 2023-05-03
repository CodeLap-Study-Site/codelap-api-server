package com.codelap.fixture;

import com.codelap.common.study.domain.Study;
import com.codelap.common.studyView.domain.StudyView;

import static com.codelap.fixture.StudyFixture.createStudy;

public class StudyViewFixture {
    public static StudyView createStudyView() {
        return StudyView.create(createStudy(), "ipAddress");
    }

    public static StudyView createStudyView(Study study) {
        return StudyView.create(study, "ipAddress");
    }
}
