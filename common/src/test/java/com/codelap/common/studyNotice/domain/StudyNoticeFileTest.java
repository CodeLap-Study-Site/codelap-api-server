package com.codelap.common.studyNotice.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StudyNoticeFileTest {
    @Test
    void 스터디_인증_파일_생성_성공() {
        StudyNoticeFile studyNoticeFile = (StudyNoticeFile) StudyNoticeFile.create();

        studyNoticeFile.update("s3ImageURL", "originalName");

        assertThat(studyNoticeFile.getS3ImageURL()).isEqualTo("s3ImageURL");
        assertThat(studyNoticeFile.getOriginalName()).isEqualTo("originalName");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__저장명이_공백_혹은_널(String s3ImageURL) {
        StudyNoticeFile studyNoticeFile = (StudyNoticeFile) StudyNoticeFile.create();

        assertThatIllegalArgumentException().isThrownBy(() -> studyNoticeFile.update(s3ImageURL, "originalName"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_인증_생성_실패__원본명이_공백_혹은_널(String originalName) {
        StudyNoticeFile studyNoticeFile = (StudyNoticeFile) StudyNoticeFile.create();

        assertThatIllegalArgumentException().isThrownBy(() -> studyNoticeFile.update("s3ImageURL", originalName));
    }
}