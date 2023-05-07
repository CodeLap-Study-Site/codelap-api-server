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
import static com.codelap.common.studyComment.domain.StudyCommentStatus.DELETED;
import static com.codelap.fixture.StudyCommentFixture.createStudyComment;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudyCommentControllerTest extends ApiTest {
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

        StudyCommentCreateRequest req = new StudyCommentCreateRequest(study.getId(), "createMessage");

        mockMvc.perform(post("/study-comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-comment/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyComment foundStudyComment = studyCommentRepository.findAll().get(1);

        assertThat(foundStudyComment.getComment()).isEqualTo(req.message());
    }

    @Test
    @WithUserDetails
    void 스터디_댓글_수정_성공() throws Exception {
        login(member);

        StudyCommentUpdateRequest req = new StudyCommentUpdateRequest(studyComment.getId(), "updatedMessage");

        mockMvc.perform(post("/study-comment/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-comment/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyComment foundStudyComment = studyCommentRepository.findById(studyComment.getId()).orElseThrow();

        assertThat(foundStudyComment.getComment()).isEqualTo(req.message());
    }

    @Test
    @WithUserDetails
    void 스터디_댓글_삭제_성공() throws Exception {
        login(member);

        StudyCommentDeleteRequest req = new StudyCommentDeleteRequest(studyComment.getId());

        mockMvc.perform(delete("/study-comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-comment/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyComment foundStudyComment = studyCommentRepository.findById(studyComment.getId()).orElseThrow();

        assertThat(foundStudyComment.getStatus()).isEqualTo(DELETED);
    }
}
