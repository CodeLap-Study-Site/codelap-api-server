package com.codelap.api.controller.studyNoticeComment;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentCreateDto.*;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyNoticeCommentControllerTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyNoticeCommentRepository studyNoticeCommentRepository;

    private StudyNoticeComment studyNoticeComment;
    private User leader;
    private User member;
    private Study study;
    private StudyNotice studyNotice;
    private List<TechStack> techStackList;

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

        StudyNoticeFile file = StudyNoticeFile.create("savedName", "originalName", 100L);
        studyNotice = StudyNotice.create(study, "title", "contents", List.of(file));

        studyNoticeComment = studyNoticeCommentRepository.save(StudyNoticeComment.create(studyNotice, member, "content"));
    }

    @Test
    void 스터디_공지_댓글_생성_성공() throws Exception {
        StudyNoticeCommentCreateRequestFileDto file = new StudyNoticeCommentCreateRequestFileDto("savedName", "originalName", 100L);
        StudyNoticeCommentCreateRequestStudyNoticeDto studyNoticeDto = new StudyNoticeCommentCreateRequestStudyNoticeDto(study, "title", "content", List.of(file));

        StudyNoticeCommentCreateRequest req = new StudyNoticeCommentCreateRequest(studyNoticeDto, member.getId(), "content");

        mockMvc.perform(post("/study-notice-comment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-notice-comment/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyNoticeComment foundStudyNoticeComment = studyNoticeCommentRepository.findById(studyNoticeComment.getId()).orElseThrow();

        assertThat(foundStudyNoticeComment.getId()).isNotNull();
        assertThat(foundStudyNoticeComment.getContent()).isEqualTo(req.content());
        assertThat(foundStudyNoticeComment.getCreateAt()).isNotNull();
        assertThat(foundStudyNoticeComment.getStatus()).isEqualTo(CREATED);
    }
}

