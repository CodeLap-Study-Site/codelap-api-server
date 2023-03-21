package com.codelap.common.studyConfirmation.service;

import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;
import com.codelap.common.user.domain.User;

import java.util.List;

public interface StudyConfirmationService {
    void create(Long studyId, Long userId, String title, String content, List<StudyConfirmationFile> files);

    void confirm(Long studyConfirmId, User leader);
}
