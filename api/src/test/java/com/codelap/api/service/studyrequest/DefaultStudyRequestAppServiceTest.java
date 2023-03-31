package com.codelap.api.service.studyrequest;

import com.codelap.common.study.domain.*;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static com.codelap.common.studyRequest.domain.StudyRequestStatus.APPROVED;
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
    private List<TechStack> techStackList;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(Java, Spring);

        study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList));

        user = userRepository.save(User.create("user", 10, career, "abcd", "email"));

        studyRequest = studyRequestRepository.save(StudyRequest.create(user, study, "참가신청"));
    }

    @Test
    void 스터디_참가_신청_수락_성공() {
        studyRequestAppService.approve(studyRequest.getId(), leader.getId());

        assertThat(studyRequest.getStatus()).isEqualTo(APPROVED);
        assertThat(study.getMembers()).contains(user);
    }
}