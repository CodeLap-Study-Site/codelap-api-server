package com.codelap.api.controller.studyRequest;

import com.codelap.api.controller.study.dto.StudyCreateDto;
import com.codelap.api.controller.studyRequest.dto.StudyRequestCreateDto;
import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyRequest.domain.StudyRequest;
import com.codelap.common.studyRequest.domain.StudyRequestRepository;
import com.codelap.common.studyRequest.domain.StudyRequestStatus;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.time.Period;

import static com.codelap.api.controller.studyRequest.dto.StudyRequestCreateDto.*;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.studyRequest.domain.StudyRequestStatus.REQUESTED;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
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

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        study = studyRepository.save(Study.create("팀", "정보", 4, HARD, period, needCareer, leader));

        user = userRepository.save(User.create("candidate", 10, career, "abcd", "email"));
    }

    @Test
    void 스터디_참가_요청_성공() throws Exception {
        StudyRequestCreateRequest req = new StudyRequestCreateRequest(user.getId(), study.getId(), "message");

        mockMvc.perform(post("/studyRequest")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("studyRequest/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyRequest foundStudyRequest = studyRequestRepository.findAll().get(0);

        Assertions.assertThat(foundStudyRequest.getStudy()).isSameAs(study);
        Assertions.assertThat(foundStudyRequest.getUser()).isSameAs(user);
        Assertions.assertThat(foundStudyRequest.getMessage()).isEqualTo(req.message());
        Assertions.assertThat(foundStudyRequest.getCreatedAt()).isNotNull();
        Assertions.assertThat(foundStudyRequest.getStatus()).isEqualTo(REQUESTED);
    }
}