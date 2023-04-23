package com.codelap.api.controller.studyComment;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyComment.domain.StudyComment;
import com.codelap.common.studyComment.domain.StudyCommentRepository;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.api.controller.studyComment.dto.StudyCommentCreateDto.StudyCommentCreateRequest;
import static com.codelap.api.controller.studyComment.dto.StudyCommentDeleteDto.StudyCommentDeleteRequest;
import static com.codelap.api.controller.studyComment.dto.StudyCommentUpdateDto.StudyCommentUpdateRequest;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static com.codelap.common.studyComment.domain.StudyCommentStatus.DELETED;
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

    @Autowired
    private StudyCommentService studyCommentService;

    private User leader;

    private User member;

    private Study study;

    private List<TechStack> techStackList;

    private StudyComment studyComment;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));
        member = userRepository.save(User.create("user", 10, career, "abcd", "email"));


        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(Java, Spring);

        study = studyRepository.save(Study.create("팀", "정보", 4, HARD, period, needCareer, leader, techStackList));

        study.addMember(member);
    }

    @Test
    void 스터디_댓글_생성_성공() throws Exception {
        StudyCommentCreateRequest req = new StudyCommentCreateRequest(study.getId(), member.getId(), "createMessage");

        mockMvc.perform(post("/study-comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-comment/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyComment foundStudyComment = studyCommentRepository.findAll().get(0);

        assertThat(foundStudyComment.getComment()).isEqualTo(req.message());
    }

    @Test
    void 스터디_댓글_수정_성공() throws Exception {
        studyComment = studyCommentRepository.save(StudyComment.create(study, member, "message"));

        StudyCommentUpdateRequest req = new StudyCommentUpdateRequest(studyComment.getId(), member.getId(), "updatedMessage");

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
    void 스터디_댓글_삭제_성공() throws Exception {
        studyComment = studyCommentRepository.save(StudyComment.create(study, member, "message"));

        StudyCommentDeleteRequest req = new StudyCommentDeleteRequest(studyComment.getId(), member.getId());

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
