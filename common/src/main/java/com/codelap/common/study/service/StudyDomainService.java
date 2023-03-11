package com.codelap.common.study.service;

import com.codelap.common.study.domain.*;
import com.codelap.common.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyDomainService implements StudyService {

    private final StudyRepository studyRepository;

    @Override
    public void create(String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer, User leader) {
        Study study = Study.create(name, info, maxMembersSize, difficulty, period, needCareer, leader);

        studyRepository.save(study);
    }
}
