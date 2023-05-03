package com.codelap.api.controller.studyConfirmation;

import com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationReConfirmDto.StudyConfirmationReConfirmRequest;
import com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationReConfirmDto.StudyConfirmationReConfirmRequestFileDto;
import com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationRejectDto.StudyConfirmationRejectRequest;
import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyConfirmation.domain.StudyConfirmation;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationRepository;
import com.codelap.common.user.domain.User;
import com.codelap.fixture.StudyConfirmationFixture;
import com.codelap.fixture.StudyFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationConfirmDto.StudyConfirmationConfirmRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequestFileDto;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationDeleteDto.StudyConfirmationDeleteRequest;
import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyConfirmationControllerTest extends ApiTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyConfirmationRepository studyConfirmationRepository;

    private User leader;
    private User member;
    private Study study;
    private StudyConfirmation studyConfirmation;

    @BeforeEach
    void setUp() {
        leader = prepareLoggedInUser();
        member = prepareLoggedInUser("email.com");

        study = studyRepository.save(StudyFixture.createStudy(leader));

        study.addMember(member);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_생성_성공() throws Exception {
        login(member.getId());

        StudyConfirmationCreateRequestFileDto file = new StudyConfirmationCreateRequestFileDto("savedName", "originalName", 100L);
        StudyConfirmationCreateRequest req = new StudyConfirmationCreateRequest(study.getId(), "title", "contents", List.of(file));

        mockMvc.perform(post("/study-confirmation")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-confirmation/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

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
        studyConfirmation = studyConfirmationRepository.save(StudyConfirmationFixture.createStudyConfirmation(study, member));

        login(leader.getId());

        StudyConfirmationConfirmRequest req = new StudyConfirmationConfirmRequest(studyConfirmation.getId());

        mockMvc.perform(post("/study-confirmation/confirm")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-confirmation/confirm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(studyConfirmation.getStatus()).isEqualTo(CONFIRMED);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_거절_성공() throws Exception {
        studyConfirmation = studyConfirmationRepository.save(StudyConfirmationFixture.createStudyConfirmation(study, member));

        login(leader.getId());

        StudyConfirmationRejectRequest req = new StudyConfirmationRejectRequest(studyConfirmation.getId());

        mockMvc.perform(post("/study-confirmation/reject")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-confirmation/reject",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(studyConfirmation.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_재인증_성공() throws Exception {
        studyConfirmation = studyConfirmationRepository.save(StudyConfirmationFixture.createStudyConfirmation(study, member));

        login(member.getId());

        studyConfirmation.setStatus(REJECTED);

        StudyConfirmationReConfirmRequestFileDto refile = new StudyConfirmationReConfirmRequestFileDto("savedName", "originalName", 100L);
        StudyConfirmationReConfirmRequest req = new StudyConfirmationReConfirmRequest(studyConfirmation.getId(), "title", "content", List.of(refile));

        mockMvc.perform(post("/study-confirmation/reconfirm")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-confirmation/reconfirm",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_삭제_성공() throws Exception {
        studyConfirmation = studyConfirmationRepository.save(StudyConfirmationFixture.createStudyConfirmation(study, member));

        login(member.getId());

        StudyConfirmationDeleteRequest req = new StudyConfirmationDeleteRequest(studyConfirmation.getId());

        mockMvc.perform(delete("/study-confirmation")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-confirmation/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(studyConfirmation.getStatus()).isEqualTo(DELETED);
    }
}