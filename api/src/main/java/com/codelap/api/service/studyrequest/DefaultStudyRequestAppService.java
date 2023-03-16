package com.codelap.api.service.studyrequest;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.service.StudyService;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.studyRequest.service.StudyRequestService;
import com.codelap.common.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DefaultStudyRequestAppService implements StudyRequestAppService {

    private final StudyRequestService studyRequestService;
    private final StudyService studyService;
    private final StudyRequestRepository studyRequestRepository;

    @Override
    public void approve(Long studyRequestId, Long leaderId) {
        StudyRequest studyRequest = studyRequestRepository.findById(studyRequestId).orElseThrow();

        Study study = studyRequest.getStudy();
        User user = studyRequest.getUser();

        studyRequestService.approve(studyRequestId, leaderId);

        studyService.addMember(study.getId(), user.getId(), leaderId);
    }
}
