package com.codelap.common.studyComment.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyComment.domain.StudyComment;
import com.codelap.common.studyComment.domain.StudyCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.codelap.common.support.Preconditions.actorValidate;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyCommentDomainService implements StudyCommentService{

    private final StudyRepository studyRepository;

    private final UserRepository userRepository;

    private final StudyCommentRepository studyCommentRepository;

    @Override
    public void create(Long studyId, Long userId, String message) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        actorValidate(study.containsMember(user));

        studyCommentRepository.save(StudyComment.create(study, user, message));
    }
}
