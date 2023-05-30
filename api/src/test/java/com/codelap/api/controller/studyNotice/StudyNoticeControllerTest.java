package com.codelap.api.controller.studyNotice;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.codelap.api.controller.studyNotice.dto.StudyNoticeCreateDto.StudyNoticeCreateRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeCreateDto.StudyNoticeCreateRequestFileDto;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeDeleteDto.StudyNoticeDeleteRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeUpdateDto.StudyNoticeUpdateRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeUpdateDto.StudyNoticeUpdateRequestFileDto;
import static com.codelap.api.support.RestDocumentationUtils.*;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.CREATED;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.DELETED;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyNoticeFixture.createStudyNotice;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyNoticeControllerTest extends ApiTest {

    private static final String DOCS_TAG = "StudyNotice";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyNoticeRepository studyNoticeRepository;

    private User leader;
    private Study study;
    private StudyNotice studyNotice;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
        study = studyRepository.save(createStudy(leader));
        studyNotice = studyNoticeRepository.save(createStudyNotice(study));
    }

    @Test
    @WithUserDetails
    void 스터디_공지_생성_성공() throws Exception {
        login(leader);

        StudyNoticeCreateRequestFileDto file = new StudyNoticeCreateRequestFileDto("s3ImageURL", "originalName");

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study-notice",
                        APPLICATION_JSON,
                        new StudyNoticeCreateRequest(study.getId(), "title", "contents", List.of(file)),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-notice/create",
                        DOCS_TAG,
                        "스터디 공지 생성",
                        null, null
                )
        ).andExpect(status().isOk());

        StudyNotice studyNotice = studyNoticeRepository.findAll().get(1);

        assertThat(studyNotice.getId()).isNotNull();
        assertThat(studyNotice.getStudy()).isSameAs(study);
        assertThat(studyNotice.getTitle()).isEqualTo("title");
        assertThat(studyNotice.getContents()).isEqualTo("contents");
        assertThat(studyNotice.getCreatedAt()).isNotNull();
        assertThat(studyNotice.getStatus()).isEqualTo(CREATED);
    }

    @Test
    @WithUserDetails
    void 스터디_공지_수정_성공() throws Exception {
        login(leader);

        StudyNoticeUpdateRequestFileDto file = new StudyNoticeUpdateRequestFileDto("s3ImageURL", "originalName");

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study-notice/update",
                        APPLICATION_JSON,
                        new StudyNoticeUpdateRequest(studyNotice.getId(), "title", "contents", List.of(file)),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-notice/update",
                        DOCS_TAG,
                        "스터디 공지 수정",
                        null, null
                )
        ).andExpect(status().isOk());

        StudyNotice foundStudyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(foundStudyNotice.getTitle()).isEqualTo("title");
        assertThat(foundStudyNotice.getContents()).isEqualTo("contents");
        assertThat(foundStudyNotice.getCreatedAt()).isNotNull();
        assertThat(foundStudyNotice.getStatus()).isEqualTo(CREATED);
    }

    @Test
    @WithUserDetails
    void 스터디_공지_삭제_성공() throws Exception {
        login(leader);

        mockMvc.perform(
                deleteMethodRequestBuilder(
                        "/study-notice",
                        APPLICATION_JSON,
                        new StudyNoticeDeleteRequest(studyNotice.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-notice/delete",
                        DOCS_TAG,
                        "스터디 공지 삭제",
                        null, null
                )
        ).andExpect(status().isOk());

        StudyNotice studyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(studyNotice.getStatus()).isEqualTo(DELETED);
    }
}