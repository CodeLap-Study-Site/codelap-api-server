package com.codelap.api.controller.studyNotice;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.api.controller.studyNotice.dto.StudyNoticeCreateDto.StudyNoticeCreateRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeCreateDto.StudyNoticeCreateRequestFileDto;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeDeleteDto.StudyNoticeDeleteRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeUpdateDto.StudyNoticeUpdateRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeUpdateDto.StudyNoticeUpdateRequestFileDto;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.CREATED;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.DELETED;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyNoticeControllerTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyNoticeRepository studyNoticeRepository;

    private User leader;
    private Study study;
    private List<StudyTechStack> techStackList;

    private StudyNotice studyNotice;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = prepareLoggedInUser();

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(new StudyTechStack(Java), new StudyTechStack(Spring));

        study = studyRepository.save(Study.create("팀", "정보", 4, HARD, period, needCareer, leader, techStackList));

        StudyNoticeFile file = StudyNoticeFile.create("savedName", "originalName", 100L);
        studyNotice = studyNoticeRepository.save(StudyNotice.create(study, "title", "contents", List.of(file)));
    }

    @Test
    @WithUserDetails
    void 스터디_공지_생성_성공() throws Exception {
        StudyNoticeCreateRequestFileDto file = new StudyNoticeCreateRequestFileDto("savedName", "originalName", 100L);
        StudyNoticeCreateRequest req = new StudyNoticeCreateRequest(study.getId(), "title", "contents", List.of(file));

        mockMvc.perform(post("/study-notice")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-notice/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyNotice studyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(studyNotice.getId()).isNotNull();
        assertThat(studyNotice.getStudy()).isSameAs(study);
        assertThat(studyNotice.getTitle()).isEqualTo(req.title());
        assertThat(studyNotice.getContents()).isEqualTo(req.contents());
        assertThat(studyNotice.getCreatedAt()).isNotNull();
        assertThat(studyNotice.getStatus()).isEqualTo(CREATED);
    }

    @Test
    @WithUserDetails
    void 스터디_공지_수정_성공() throws Exception {
        StudyNoticeUpdateRequestFileDto file = new StudyNoticeUpdateRequestFileDto("savedName", "originalName", 100L);

        StudyNoticeUpdateRequest req = new StudyNoticeUpdateRequest(studyNotice.getId(), "title", "contents", List.of(file));

        mockMvc.perform(post("/study-notice/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-notice/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyNotice foundStudyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(foundStudyNotice.getTitle()).isEqualTo(req.title());
        assertThat(foundStudyNotice.getContents()).isEqualTo(req.contents());
        assertThat(foundStudyNotice.getCreatedAt()).isNotNull();
        assertThat(foundStudyNotice.getStatus()).isEqualTo(CREATED);
    }

    @Test
    @WithUserDetails
    void 스터디_공지_삭제_성공() throws Exception {
        StudyNoticeDeleteRequest req = new StudyNoticeDeleteRequest(studyNotice.getId());

        mockMvc.perform(delete("/study-notice")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-notice/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyNotice studyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(studyNotice.getStatus()).isEqualTo(DELETED);
    }
}