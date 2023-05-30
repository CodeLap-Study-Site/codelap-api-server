package com.codelap.api.controller.studyComment;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyComment.domain.StudyComment;
import com.codelap.common.studyComment.domain.StudyCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.codelap.api.controller.studyComment.dto.StudyCommentCreateDto.StudyCommentCreateRequest;
import static com.codelap.api.controller.studyComment.dto.StudyCommentDeleteDto.StudyCommentDeleteRequest;
import static com.codelap.api.controller.studyComment.dto.StudyCommentUpdateDto.StudyCommentUpdateRequest;
import static com.codelap.api.support.RestDocumentationUtils.*;
import static com.codelap.common.studyComment.domain.StudyCommentStatus.DELETED;
import static com.codelap.fixture.StudyCommentFixture.createStudyComment;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudyCommentControllerTest extends ApiTest {

    private static final String DOCS_TAG = "StudyComment";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyCommentRepository studyCommentRepository;

    private User leader;
    private User member;
    private Study study;
    private StudyComment studyComment;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());

        study = studyRepository.save(createStudy(leader));
        study.addMember(member);

        studyComment = studyCommentRepository.save(createStudyComment(study, member));
    }

    @Test
    @WithUserDetails
    void 스터디_댓글_생성_성공() throws Exception {
        login(member);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study-comment",
                        APPLICATION_JSON,
                        new StudyCommentCreateRequest(study.getId(), "createMessage"),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-comment/create",
                        DOCS_TAG,
                        "스터디 댓글 생성",
                        null, null
                )
        ).andExpect(status().isOk());

        StudyComment foundStudyComment = studyCommentRepository.findAll().get(1);

        assertThat(foundStudyComment.getComment()).isEqualTo("createMessage");
    }

    @Test
    @WithUserDetails
    void 스터디_댓글_수정_성공() throws Exception {
        login(member);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study-comment/update",
                        APPLICATION_JSON,
                        new StudyCommentUpdateRequest(studyComment.getId(), "updatedMessage"),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-comment/update",
                        DOCS_TAG,
                        "스터디 댓글 수정",
                        null, null
                )
        ).andExpect(status().isOk());

        StudyComment foundStudyComment = studyCommentRepository.findById(studyComment.getId()).orElseThrow();

        assertThat(foundStudyComment.getComment()).isEqualTo("updatedMessage");
    }

    @Test
    @WithUserDetails
    void 스터디_댓글_삭제_성공() throws Exception {
        login(member);

        mockMvc.perform(
                deleteMethodRequestBuilder(
                        "/study-comment",
                        APPLICATION_JSON,
                        new StudyCommentDeleteRequest(studyComment.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study-comment/delete",
                        DOCS_TAG,
                        "스터디 댓글 삭제",
                        null, null
                )
        ).andExpect(status().isOk());

        StudyComment foundStudyComment = studyCommentRepository.findById(studyComment.getId()).orElseThrow();

        assertThat(foundStudyComment.getStatus()).isEqualTo(DELETED);
    }
}
