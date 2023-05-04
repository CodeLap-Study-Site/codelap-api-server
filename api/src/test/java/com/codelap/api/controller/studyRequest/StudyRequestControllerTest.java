package com.codelap.api.controller.studyRequest;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.codelap.api.controller.studyRequest.dto.StudyRequestApproveDto.StudyRequestApproveRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestCancelDto.StudyRequestCancelRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestCreateDto.StudyRequestCreateRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestRejectDto.StudyRequestRejectRequest;
import static com.codelap.api.support.HttpMethod.POST;
import static com.codelap.common.studyRequest.domain.StudyRequestStatus.*;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyRequestFixture.createStudyRequest;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

class StudyRequestControllerTest extends ApiTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyRequestRepository studyRequestRepository;

    private User leader;
    private User user;
    private Study study;
    private StudyRequest studyRequest;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
        user = userRepository.save(createActivateUser());
        study = studyRepository.save(createStudy(leader));

        studyRequest = studyRequestRepository.save(createStudyRequest(study, user));
    }

    @Test
    @WithUserDetails
    void 스터디_참가_요청_성공() throws Exception {
        login(user);

        StudyRequestCreateRequest req = new StudyRequestCreateRequest(study.getId(), "message");

        setMockMvcPerform(POST, req, "/study-request", "study-request/create");

        StudyRequest foundStudyRequest = studyRequestRepository.findAll().get(1);

        assertThat(foundStudyRequest.getId()).isNotNull();
        assertThat(foundStudyRequest.getStudy()).isSameAs(study);
        assertThat(foundStudyRequest.getUser()).isSameAs(user);
        assertThat(foundStudyRequest.getMessage()).isEqualTo(req.message());
        assertThat(foundStudyRequest.getCreatedAt()).isNotNull();
        assertThat(foundStudyRequest.getStatus()).isEqualTo(REQUESTED);
    }

    @Test
    @WithUserDetails
    void 스터디_참가_요청_승인_성공() throws Exception {
        login(leader);

        StudyRequestApproveRequest req = new StudyRequestApproveRequest(studyRequest.getId());

        setMockMvcPerform(POST, req, "/study-request/approve");

        StudyRequest foundStudyRequest = studyRequestRepository.findById(studyRequest.getId()).orElseThrow();

        assertThat(foundStudyRequest.getStatus()).isEqualTo(APPROVED);
        assertThat(study.getMembers()).contains(user);
    }

    @Test
    @WithUserDetails
    void 스터디_참가_요청_거절_성공() throws Exception {
        login(leader);

        StudyRequestRejectRequest req = new StudyRequestRejectRequest(studyRequest.getId(), "거절 메세지");

        setMockMvcPerform(POST, req, "/study-request/reject");

        StudyRequest foundStudyRequest = studyRequestRepository.findById(studyRequest.getId()).orElseThrow();

        assertThat(foundStudyRequest.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    @WithUserDetails
    void 스터디_참가_요청_취소_성공() throws Exception {
        login(user);

        StudyRequestCancelRequest req = new StudyRequestCancelRequest(studyRequest.getId());

        setMockMvcPerform(POST ,req, "/study-request/cancel");

        StudyRequest foundStudyRequest = studyRequestRepository.findById(studyRequest.getId()).orElseThrow();

        assertThat(foundStudyRequest.getStatus()).isEqualTo(CANCELED);
    }
}