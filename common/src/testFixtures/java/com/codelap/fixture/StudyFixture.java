package com.codelap.fixture;

import com.codelap.common.study.domain.*;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static com.codelap.fixture.UserFixture.createUser;

public class StudyFixture {
    public static Study createStudy() {
        return Study.create(
                "name", "info", 4, NORMAL,
                createStudyPeriod(), createStudyNeedCareer(),
                createUser(), createStudyTechStacks(), createStudyFiles()
        );
    }

    public static Study createStudy(int maxMemberSize) {
        return Study.create(
                "name", "info", maxMemberSize, NORMAL,
                createStudyPeriod(), createStudyNeedCareer(),
                createUser(), createStudyTechStacks(), createStudyFiles()
        );
    }

    public static Study createStudy(User leader) {
        return Study.create(
                "name", "info", 4, NORMAL,
                createStudyPeriod(), createStudyNeedCareer(),
                leader, createStudyTechStacks(), createStudyFiles()
        );
    }

    public static Study createStudy(User leader, TechStack... techStacks) {
        return Study.create(
                "name", "info", 4, NORMAL,
                createStudyPeriod(), createStudyNeedCareer(),
                leader, Arrays.stream(techStacks)
                        .map(StudyTechStack::new)
                        .collect(Collectors.toList())
                , createStudyFiles()
        );
    }

    public static StudyPeriod createStudyPeriod() {
        return StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
    }

    public static StudyNeedCareer createStudyNeedCareer() {
        return StudyNeedCareer.create("occupation", 1);
    }

    public static List<StudyTechStack> createStudyTechStacks() {
        return Arrays.asList(new StudyTechStack(Java), new StudyTechStack(Spring));
    }

    public static List<StudyFile> createStudyFiles() {
        StudyFile studyFile = StudyFile.create();
        return Arrays.asList(studyFile.update("s3ImageURL", "originalName"));
    }
}
