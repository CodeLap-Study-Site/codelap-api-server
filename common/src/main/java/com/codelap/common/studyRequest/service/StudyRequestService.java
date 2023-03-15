package com.codelap.common.studyRequest.service;

import com.codelap.common.studyRequest.domain.StudyRequest;

public interface StudyRequestService {

    void create(Long userId, Long studyId, String message);

    void approve(Long studyRequestId, Long leaderId, Long studyId);
}
