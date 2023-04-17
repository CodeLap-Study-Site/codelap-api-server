package com.codelap.common.studyNotice.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codelap.common.support.Preconditions.actorValidate;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyNoticeDomainService implements StudyNoticeService {

    private final UserRepository userRepository;

    private final StudyRepository studyRepository;

    private final StudyNoticeRepository studyNoticeRepository;

    @Override
    public StudyNotice create(Long studyId, Long leaderId, String title, String contents, List<StudyNoticeFile> files) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        User leader = userRepository.findById(leaderId).orElseThrow();

        actorValidate(study.isLeader(leader));

        return studyNoticeRepository.save(StudyNotice.create(study, title, contents, files));
    }

    @Override
    public void update(Long studyNoticeId, Long leaderId, String title, String contents, List<StudyNoticeFile> files) {
        User leader = userRepository.findById(leaderId).orElseThrow();
        StudyNotice studyNotice = studyNoticeRepository.findById(studyNoticeId).orElseThrow();

        actorValidate(studyNotice.isLeader(leader));

        studyNotice.update(title, contents, files);
    }
}
