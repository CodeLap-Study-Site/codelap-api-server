package com.codelap.common.studyNoticeComment.service;

import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyNoticeCommentDomainService implements StudyNoticeCommentService {

    private final StudyNoticeCommentRepository studyNoticeCommentRepository;
    private final UserRepository userRepository;


    @Override
    public StudyNoticeComment create(StudyNotice studyNotice, Long userId, String content) {
        User user = userRepository.findById(userId).orElseThrow();

        StudyNoticeComment studyNoticeComment = StudyNoticeComment.create(studyNotice, user, content);

        return studyNoticeCommentRepository.save(studyNoticeComment);
    }
}