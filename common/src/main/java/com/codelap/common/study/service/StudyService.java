package com.codelap.common.study.service;

import com.codelap.common.study.domain.StudyDifficulty;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.TechStack;

import java.util.List;

public interface StudyService {

    void create(Long leaderId, String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer, List<TechStack> techStackList);

    void update(Long studyId, Long userId, String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer, List<TechStack> techStackList);

    void addMember(Long studyId, Long userId, Long leaderId);

    void proceed(Long studyId, Long userId);

    void removeMember(Long studyId, Long memberId, Long leaderId);

    void close(Long studyId, Long leaderId);

    void leave(Long studyId, Long memberId);

    void delete(Long studyId, Long leaderId);

    void open(Long studyId, Long leaderId, StudyPeriod period);
}
