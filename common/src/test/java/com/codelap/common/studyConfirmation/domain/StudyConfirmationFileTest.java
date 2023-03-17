package com.codelap.common.studyConfirmation.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyConfirmationFileTest {

    private static final Long MIN_SIZE = 1L;

    @Test
    void 스터디_인증_파일_생성_성공() {
        StudyConfirmationFile studyConfirmationFile = StudyConfirmationFile.create("savedName", "originalName", 100L);

        assertThat(studyConfirmationFile.getSavedName()).isEqualTo("savedName");
        assertThat(studyConfirmationFile.getOriginalName()).isEqualTo("originalName");
        assertThat(studyConfirmationFile.getSize()).isEqualTo(100L);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__저장명이_공백_혹은_널(String savedName) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyConfirmationFile.create(savedName, "originalName", 100L));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__원본명이_공백_혹은_널(String originalName) {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyConfirmationFile.create("savedName", originalName, 100L));
    }

    @Test
    void 스터디_인증_생성_실패__원본명이_공백_혹은_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyConfirmationFile.create("savedName", "originalName", MIN_SIZE - 1));
    }
}