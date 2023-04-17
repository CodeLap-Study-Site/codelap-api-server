package com.codelap.common.studyNotice.service;

import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;

import java.util.List;

public interface StudyNoticeService {
    StudyNotice create(Long studyId, Long leaderId, String title, String contents, List<StudyNoticeFile> files);

    void update(Long studyNoticeId, Long leaderId, String title, String contents, List<StudyNoticeFile> files);
}
