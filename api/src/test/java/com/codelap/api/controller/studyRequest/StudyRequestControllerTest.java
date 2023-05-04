package com.codelap.api.controller.studyRequest;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.api.controller.studyRequest.dto.StudyRequestApproveDto.StudyRequestApproveRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestCancelDto.StudyRequestCancelRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestCreateDto.StudyRequestCreateRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestRejectDto.StudyRequestRejectRequest;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.studyRequest.domain.StudyRequestStatus.*;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private List<StudyTechStack> techStackList;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(createActivateUser());

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(new StudyTechStack(Java), new StudyTechStack(Spring));

        study = studyRepository.save(Study.create("팀", "정보", 4, HARD, period, needCareer, leader, techStackList));

        user = userRepository.save(createActivateUser());

        studyRequest = studyRequestRepository.save(StudyRequest.create(user, study, "message"));
    }

    @Test
    void 스터디_참가_요청_성공() throws Exception {
        StudyRequestCreateRequest req = new StudyRequestCreateRequest(user.getId(), study.getId(), "message");

        mockMvc.perform(post("/study-request")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-request/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyRequest foundStudyRequest = studyRequestRepository.findAll().get(1);

        assertThat(foundStudyRequest.getId()).isNotNull();
        assertThat(foundStudyRequest.getStudy()).isSameAs(study);
        assertThat(foundStudyRequest.getUser()).isSameAs(user);
        assertThat(foundStudyRequest.getMessage()).isEqualTo(req.message());
        assertThat(foundStudyRequest.getCreatedAt()).isNotNull();
        assertThat(foundStudyRequest.getStatus()).isEqualTo(REQUESTED);
    }

    @Test
    void 스터디_참가_요청_승인_성공() throws Exception {
        StudyRequestApproveRequest req = new StudyRequestApproveRequest(studyRequest.getId(), leader.getId());

        mockMvc.perform(post("/study-request/approve")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-request/approve",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyRequest foundStudyRequest = studyRequestRepository.findById(studyRequest.getId()).orElseThrow();

        assertThat(foundStudyRequest.getStatus()).isEqualTo(APPROVED);
        assertThat(study.getMembers()).contains(user);
    }

    @Test
    void 스터디_참가_요청_거절_성공() throws Exception {
        StudyRequestRejectRequest req = new StudyRequestRejectRequest(studyRequest.getId(), leader.getId(), "거절 메세지");

        mockMvc.perform(post("/study-request/reject")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-request/reject",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyRequest foundStudyRequest = studyRequestRepository.findById(studyRequest.getId()).orElseThrow();

        assertThat(foundStudyRequest.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    void 스터디_참가_요청_취소_성공() throws Exception {
        StudyRequestCancelRequest req = new StudyRequestCancelRequest(studyRequest.getId(), user.getId());

        mockMvc.perform(post("/study-request/cancel")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-request/cancel",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyRequest foundStudyRequest = studyRequestRepository.findById(studyRequest.getId()).orElseThrow();

        assertThat(foundStudyRequest.getStatus()).isEqualTo(CANCELED);
    }
}