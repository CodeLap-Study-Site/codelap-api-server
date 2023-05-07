package com.codelap.api.service.studyrequest;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.codelap.common.studyRequest.domain.StudyRequestStatus.APPROVED;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyRequestFixture.createStudyRequest;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class DefaultStudyRequestAppServiceTest {

    @Autowired
    private StudyRequestAppService studyRequestAppService;

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
        leader = userRepository.save(createActivateUser());
        user = userRepository.save(createActivateUser());
        study = studyRepository.save(createStudy(leader));
        studyRequest = studyRequestRepository.save(createStudyRequest(study, user));
    }

    @Test
    void 스터디_참가_신청_수락_성공() {
        studyRequestAppService.approve(studyRequest.getId(), leader.getId());

        assertThat(studyRequest.getStatus()).isEqualTo(APPROVED);
        assertThat(study.getMembers()).contains(user);
    }
}