package com.codelap.fixture;

import com.codelap.common.study.domain.Study;
import com.codelap.common.studyConfirmation.domain.StudyConfirmation;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;
import com.codelap.common.user.domain.User;

import java.util.List;

import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createUser;

public class StudyConfirmationFixture {
    public static StudyConfirmation createStudyConfirmation() {
        return StudyConfirmation.create(createStudy(), createUser(), "title", "content", createStudyConfirmationFiles());
    }

    public static StudyConfirmation createStudyConfirmation(Study study, User user) {
        return StudyConfirmation.create(study, user, "title", "content", createStudyConfirmationFiles());
    }

    public static List<StudyConfirmationFile> createStudyConfirmationFiles() {

        StudyConfirmationFile studyConfirmationFile = (StudyConfirmationFile) StudyConfirmationFile.create();

        studyConfirmationFile.update("s3ImageURL", "originalName");

        return List.of(studyConfirmationFile, studyConfirmationFile);
    }
}
