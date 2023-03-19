package com.codelap.common.study.service;

import com.codelap.common.study.domain.*;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.codelap.common.support.Preconditions.require;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyDomainService implements StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    @Override
    public void create(Long leaderId, String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer) {
        User leader = userRepository.findById(leaderId).orElseThrow();

        Study study = Study.create(name, info, maxMembersSize, difficulty, period, needCareer, leader);

        studyRepository.save(study);
    }

    @Override
    public void update(Long studyId, Long userId, String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        User leader = userRepository.findById(userId).orElseThrow();

        require(study.isLeader(leader));

        study.update(name, info, maxMembersSize, difficulty, period, needCareer);
    }

    @Override
    public void addMember(Long studyId, Long userId, Long leaderId) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        User leader = userRepository.findById(leaderId).orElseThrow();

        require(study.isLeader(leader));

        study.addMember(user);
    }

    @Override
    public void proceed(Long studyId, Long userId) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        require(study.isLeader(user));

        study.proceed();
    }

    @Override
    public void removeMember(Long studyId, Long memberId, Long leaderId) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        User member = userRepository.findById(memberId).orElseThrow();
        User leader = userRepository.findById(leaderId).orElseThrow();

        require(study.isLeader(leader));

        study.removeMember(member);
    }
}
