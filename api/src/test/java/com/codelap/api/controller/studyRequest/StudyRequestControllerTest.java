package com.codelap.api.controller.studyRequest;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;

import static com.codelap.api.controller.studyRequest.dto.StudyRequestApproveDto.StudyRequestApproveRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestCreateDto.StudyRequestCreateRequest;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.studyRequest.domain.StudyRequestStatus.APPROVED;
import static com.codelap.common.studyRequest.domain.StudyRequestStatus.REQUESTED;
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

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        study = studyRepository.save(Study.create("팀", "정보", 4, HARD, period, needCareer, leader));

        user = userRepository.save(User.create("candidate", 10, career, "abcd", "email"));

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
    }
}