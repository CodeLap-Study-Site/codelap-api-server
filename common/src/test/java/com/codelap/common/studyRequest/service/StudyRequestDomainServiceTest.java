package com.codelap.common.studyRequest.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codelap.common.studyRequest.domain.StudyRequestStatus.*;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyRequestFixture.createStudyRequest;
import static com.codelap.fixture.UserFixture.createActivateUser;
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
    private StudyRequestRepository studyRequestRepository;

    private Study study;
    private User user;
    private User leader;
    private StudyRequest studyRequest;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser("leader"));
        user = userRepository.save(createActivateUser("user"));

        study = studyRepository.save(createStudy(leader));

        studyRequest = studyRequestRepository.save(createStudyRequest(study, user));
    }

    @Test
    void 스터디_참가_신청_성공() {
        studyRequestService.create(user.getId(), study.getId(), "참가신청");

        StudyRequest studyRequest = studyRequestRepository.findAll().get(1);

        assertThat(studyRequest.getId()).isNotNull();
    }

    @Test
    void 스터디_참가_신청_수락_성공() {
        studyRequestService.approve(studyRequest.getId(), leader.getId());

        assertThat(studyRequestRepository.findAll().get(0).getStatus()).isEqualTo(APPROVED);
    }

    @Test
    void 스터디_참가_신청_수락_실패__스터디의_리더가_아님() {
        User fakeLeader = userRepository.save(createActivateUser("fakeLeader"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyRequestService.approve(studyRequest.getId(), fakeLeader.getId()));
    }

    @Test
    void 스터디_참가_신청_취소_성공() {
        studyRequestService.cancel(studyRequest.getId(), user.getId());

        assertThat(studyRequest.getStatus()).isEqualTo(CANCELED);
    }

    @Test
    void 스터디_참가_신청_취소__실패_스터디_요청을_보낸_사용자가_아님() {
        User fakeUser = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() ->
                studyRequestService.cancel(studyRequest.getId(), fakeUser.getId())
        );
    }

    @Test
    void 스터디_참가_신청_거절_성공() {
        studyRequestService.reject(studyRequest.getId(), leader.getId(), "참가신청 거절");

        assertThat(studyRequestRepository.findAll().get(0).getStatus()).isEqualTo(REJECTED);
    }

    @Test
    void 스터디_참가_신청_거절_실패__스터디의_리더가_아님() {
        User fakeLeader = userRepository.save(createActivateUser("fakeLeader"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyRequestService.reject(studyRequest.getId(), fakeLeader.getId(), "참가신청 거절"));
    }
}