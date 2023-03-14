package com.codelap.common.studyRequest.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class StudyRequestDomainServiceTest {

    @Autowired
    private StudyRequestService studyRequestService;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    StudyRequestRepository studyRequestRepository;

    private Study study;
    private User user;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        User leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));

        user = userRepository.save(User.create("user", 10, career, "abcd", "email"));
    }

    @Test
    void 스터디_참가_신청_성공() {
        studyRequestService.create(user.getId(), study.getId(), "참가신청");

        StudyRequest studyRequest = studyRequestRepository.findAll().get(0);

        assertThat(studyRequest.getId()).isNotNull();
    }
}