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
import static com.codelap.common.studyRequest.domain.StudyRequestStatus.APPROVED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

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
    private User leader;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

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

    @Test
    void 스터디_참가_신청_수락_성공() {
        studyRequestService.create(user.getId(), study.getId(), "참가신청");

        StudyRequest studyRequest = studyRequestRepository.findAll().get(0);

        studyRequestService.approve(studyRequest.getId(), leader.getId(), study.getId());

        assertThat(studyRequestRepository.findAll().get(0).getStatus()).isEqualTo(APPROVED);
    }

    @Test
    void 스터디_참가_신청_수락_실패__스터디의_리더가_아님() {
        studyRequestService.create(user.getId(), study.getId(), "참가신청");

        StudyRequest studyRequest = studyRequestRepository.findAll().get(0);

        UserCareer career = UserCareer.create("직무", 1);
        User fakeLeader = userRepository.save(User.create("fakeLeader", 10, career, "abcd", "fakeLeader"));

        assertThatIllegalArgumentException().isThrownBy(() -> studyRequestService.approve(studyRequest.getId(), fakeLeader.getId(), study.getId()));
    }
}