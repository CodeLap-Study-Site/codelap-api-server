package com.codelap.api.service.studyrequest;

public interface StudyRequestAppService {
    void approve(Long studyRequestId, Long leaderId);
}
