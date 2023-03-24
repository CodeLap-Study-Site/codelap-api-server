package com.codelap.common.studyNotice.service;

import com.codelap.common.studyNotice.domain.StudyNoticeFile;

import java.util.List;

public interface StudyNoticeService {
    void create(Long studyId, Long leaderId, String title, String contents, List<StudyNoticeFile> files);
}
