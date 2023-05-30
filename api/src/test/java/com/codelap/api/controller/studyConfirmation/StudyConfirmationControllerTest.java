package com.codelap.api.controller.studyConfirmation;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyConfirmation.domain.StudyConfirmation;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationConfirmDto.StudyConfirmationConfirmRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequestFileDto;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationReConfirmDto.StudyConfirmationReConfirmRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationReConfirmDto.StudyConfirmationReConfirmRequestFileDto;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationRejectDto.StudyConfirmationRejectRequest;
import static com.codelap.api.support.RestDocumentationUtils.getRestDocumentationResult;
import static com.codelap.api.support.RestDocumentationUtils.postMethodRequestBuilder;
import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.*;
import static com.codelap.fixture.StudyConfirmationFixture.createStudyConfirmation;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyConfirmationControllerTest extends ApiTest {

    private static final String DOCS_TAG = "StudyConfirmation";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyConfirmationRepository studyConfirmationRepository;

    private User leader;
    private User member;
    private Study study;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());
        study = studyRepository.save(createStudy(leader));

        study.addMember(member);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_생성_성공() throws Exception {
        login(member);

        StudyConfirmationCreateRequestFileDto file = new StudyConfirmationCreateRequestFileDto("savedName", "originalName", 100L);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study-confirmation",
                        APPLICATION_JSON,
                        new StudyConfirmationCreateRequest(study.getId(), "title", "contents", List.of(file)),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-confirmation/create",
                        DOCS_TAG,
                        "스터디 인증 생성",
                        null, null
                )
        ).andExpect(status().isOk());

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        assertThat(studyConfirmation.getId()).isNotNull();
        assertThat(studyConfirmation.getStudy()).isSameAs(study);
        assertThat(studyConfirmation.getUser()).isSameAs(member);
        assertThat(studyConfirmation.getTitle()).isEqualTo("title");
        assertThat(studyConfirmation.getContent()).isEqualTo("contents");
        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
        assertThat(studyConfirmation.getCreatedAt()).isNotNull();
    }

    @Test
    @WithUserDetails
    void 스터디_인증_확인_성공() throws Exception {
        login(leader);

        StudyConfirmation studyConfirmation = studyConfirmationRepository.save(createStudyConfirmation(study, member));

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study-confirmation/confirm",
                        APPLICATION_JSON,
                        new StudyConfirmationConfirmRequest(studyConfirmation.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-confirmation/confirm",
                        DOCS_TAG,
                        "스터디 인증 확인",
                        null, null
                )
        ).andExpect(status().isOk());

        assertThat(studyConfirmation.getStatus()).isEqualTo(CONFIRMED);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_거절_성공() throws Exception {
        login(leader);

        StudyConfirmation studyConfirmation = studyConfirmationRepository.save(createStudyConfirmation(study, member));

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study-confirmation/reject",
                        APPLICATION_JSON,
                        new StudyConfirmationRejectRequest(studyConfirmation.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-confirmation/reject",
                        DOCS_TAG,
                        "스터디 인증 거절",
                        null, null
                )
        ).andExpect(status().isOk());

        assertThat(studyConfirmation.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_재인증_성공() throws Exception {
        login(member);

        StudyConfirmation studyConfirmation = studyConfirmationRepository.save(createStudyConfirmation(study, member));

        studyConfirmation.setStatus(REJECTED);

        StudyConfirmationReConfirmRequestFileDto refile = new StudyConfirmationReConfirmRequestFileDto("savedName", "originalName", 100L);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study-confirmation/reconfirm",
                        APPLICATION_JSON,
                        new StudyConfirmationReConfirmRequest(studyConfirmation.getId(), "title", "content", List.of(refile)),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-confirmation/reconfirm",
                        DOCS_TAG,
                        "스터디 인증 거절",
                        null, null
                )
        ).andExpect(status().isOk());

        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
    }
}