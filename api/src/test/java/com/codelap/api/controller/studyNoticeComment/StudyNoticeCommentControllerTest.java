package com.codelap.api.controller.studyNoticeComment;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentCreateDto.StudyNoticeCommentCreateRequest;
import static com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentDeleteDto.StudyNoticeCommentDeleteRequest;
import static com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentUpdateDto.StudyNoticeCommentUpdateReqeust;
import static com.codelap.api.support.HttpMethod.DELETE;
import static com.codelap.api.support.HttpMethod.POST;
import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.CREATED;
import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.DELETED;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyNoticeCommentFixture.createStudyNoticeComment;
import static com.codelap.fixture.StudyNoticeFixture.createStudyNotice;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

class StudyNoticeCommentControllerTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyNoticeRepository studyNoticeRepository;

    @Autowired
    private StudyNoticeCommentRepository studyNoticeCommentRepository;

    private StudyNoticeComment studyNoticeComment;
    private User leader;
    private User member;
    private Study study;
    private StudyNotice studyNotice;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());

        study = studyRepository.save(createStudy(leader));
        study.addMember(member);

        studyNotice = studyNoticeRepository.save(createStudyNotice(study));
        studyNoticeComment = studyNoticeCommentRepository.save(createStudyNoticeComment(studyNotice, member));
    }

    @Test
    @WithUserDetails
    void 스터디_공지_댓글_생성_성공() throws Exception {
        login(member);

        StudyNoticeCommentCreateRequest req = new StudyNoticeCommentCreateRequest(studyNotice.getId(), "content");

        setMockMvcPerform(POST, req, "/study-notice-comment", "study-notice-comment/create");

        StudyNoticeComment foundStudyNoticeComment = studyNoticeCommentRepository.findById(studyNoticeComment.getId()).orElseThrow();

        assertThat(foundStudyNoticeComment.getId()).isNotNull();
        assertThat(foundStudyNoticeComment.getContent()).isEqualTo(req.content());
        assertThat(foundStudyNoticeComment.getCreateAt()).isNotNull();
        assertThat(foundStudyNoticeComment.getStatus()).isEqualTo(CREATED);
    }

    @Test
    @WithUserDetails
    void 스터디_공지_댓글_삭제_성공() throws Exception {
        login(member);

        StudyNoticeCommentDeleteRequest req = new StudyNoticeCommentDeleteRequest(studyNoticeComment.getId());

        setMockMvcPerform(DELETE, req, "/study-notice-comment", "study-notice-comment/delete");

        StudyNoticeComment foundStudyNoticeComment = studyNoticeCommentRepository.findById(studyNoticeComment.getId()).orElseThrow();
        assertThat(foundStudyNoticeComment.getStatus()).isEqualTo(DELETED);
    }

    @Test
    @WithUserDetails
    void 스터디_공지_댓글_수정_성공() throws Exception {
        login(member);

        StudyNoticeCommentUpdateReqeust req = new StudyNoticeCommentUpdateReqeust(studyNoticeComment.getId(), "content");

        setMockMvcPerform(POST, req, "/study-notice-comment/update");

        StudyNoticeComment foundStudyNoticeComment = studyNoticeCommentRepository.findById(studyNoticeComment.getId()).orElseThrow();
        assertThat(foundStudyNoticeComment.getContent()).isEqualTo("content");
    }
}

