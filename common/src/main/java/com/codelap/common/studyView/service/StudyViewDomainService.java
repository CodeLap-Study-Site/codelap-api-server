package com.codelap.common.studyView.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyView.domain.StudyView;
import com.codelap.common.studyView.domain.StudyViewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyViewDomainService implements StudyViewService{

    private final StudyRepository studyRepository;

    private final StudyViewRepository studyViewRepository;

    @Override
    public void create(Long studyId, String ipAddress) {
        Study study = studyRepository.findById(studyId).orElseThrow();

        studyViewRepository.save(StudyView.create(study, ipAddress));
    }
}
