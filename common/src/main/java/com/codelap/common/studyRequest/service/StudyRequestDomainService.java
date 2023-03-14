package com.codelap.common.studyRequest.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyRequestDomainService implements StudyRequestService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;
    private final StudyRequestRepository studyRequestRepository;

    @Override
    public void create(Long userId, Long studyId, String message) {
        User user = userRepository.findById(userId).orElseThrow();
        Study study = studyRepository.findById(studyId).orElseThrow();

        StudyRequest studyRequest = StudyRequest.create(user, study, message);

        studyRequestRepository.save(studyRequest);
    }
}
