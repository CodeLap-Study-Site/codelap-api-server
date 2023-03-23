package com.codelap.common.studyConfirmation.service;

import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;

import java.util.List;

public interface StudyConfirmationService {
    void create(Long studyId, Long userId, String title, String content, List<StudyConfirmationFile> files);

    void confirm(Long studyConfirmId, Long leaderId);

    void reject(Long studyConfirmId, Long leaderId);
}
