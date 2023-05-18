package com.codelap.fixture;

import com.codelap.common.study.domain.Study;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;

import java.util.List;

import static com.codelap.fixture.StudyFixture.createStudy;

public class StudyNoticeFixture {
    public static StudyNotice createStudyNotice() {
        return StudyNotice.create(createStudy(), "title", "content", createStudyNoticeFiles());
    }

    public static StudyNotice createStudyNotice(Study study) {
        return StudyNotice.create(study, "title", "content", createStudyNoticeFiles());
    }

    public static List<StudyNoticeFile> createStudyNoticeFiles() {
        StudyNoticeFile file = (StudyNoticeFile) StudyNoticeFile.create();
        file.update("s3ImageURL", "originalName");

        return List.of(file, file);
    }
}
