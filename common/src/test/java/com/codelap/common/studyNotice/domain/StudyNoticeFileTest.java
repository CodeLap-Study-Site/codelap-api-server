package com.codelap.common.studyNotice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static com.codelap.fixture.StudyNoticeFixture.createStudyNoticeFiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyNoticeFileTest {
    private static final Long MIN_SIZE = 1L;

    @Test
    void 스터디_인증_파일_생성_성공() {
        StudyNoticeFile studyNoticeFile = StudyNoticeFile.create("savedName", "originalName", 100L);

        assertThat(studyNoticeFile.getSavedName()).isEqualTo("savedName");
        assertThat(studyNoticeFile.getOriginalName()).isEqualTo("originalName");
        assertThat(studyNoticeFile.getSize()).isEqualTo(100L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__저장명이_공백_혹은_널(String savedName) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyNoticeFile.create(savedName, "originalName", 100L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__원본명이_공백_혹은_널(String originalName) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyNoticeFile.create("savedName", originalName, 100L));
    }

    @Test
    void 스터디_인증_생성_실패__원본명이_공백_혹은_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyNoticeFile.create("savedName", "originalName", MIN_SIZE - 1));
    }

}