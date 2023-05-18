package com.codelap.common.studyConfirmation.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyConfirmationFileTest {
    @Test
    void 스터디_인증_파일_생성_성공() {
        StudyConfirmationFile studyConfirmationFile = (StudyConfirmationFile) StudyConfirmationFile.create();

        studyConfirmationFile.update("s3ImageUrl", "originalName");

        assertThat(studyConfirmationFile.getS3ImageURL()).isEqualTo("s3ImageUrl");
        assertThat(studyConfirmationFile.getOriginalName()).isEqualTo("originalName");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__저장명이_공백_혹은_널(String savedName) {
        StudyConfirmationFile studyConfirmationFile = (StudyConfirmationFile) StudyConfirmationFile.create();

        assertThatIllegalArgumentException().isThrownBy(() -> studyConfirmationFile.update(savedName, "originalName"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__원본명이_공백_혹은_널(String originalName) {
        StudyConfirmationFile studyConfirmationFile = (StudyConfirmationFile) StudyConfirmationFile.create();

        assertThatIllegalArgumentException().isThrownBy(() -> studyConfirmationFile.update("savedName", originalName));
    }
}