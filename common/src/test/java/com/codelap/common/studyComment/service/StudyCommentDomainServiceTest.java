package com.codelap.common.studyComment.service;

import com.codelap.common.study.domain.*;
import com.codelap.common.studyComment.domain.StudyComment;
import com.codelap.common.studyComment.domain.StudyCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.fixture.StudyCommentFixture;
import com.codelap.fixture.StudyFixture;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.studyComment.domain.StudyCommentStatus.DELETED;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static com.codelap.fixture.StudyCommentFixture.*;
import static com.codelap.fixture.StudyFixture.*;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class StudyCommentDomainServiceTest {

    @Autowired
    private StudyCommentService studyCommentService;

    @Autowired
    private StudyCommentRepository studyCommentRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    private User leader;
    private Study study;
    private StudyComment studyComment;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());

        study = studyRepository.save(createStudy(leader));
    }

    @Test
    void 스터디_댓글_생성_성공() {
        studyCommentService.create(study.getId(), leader.getId(), "message");

        studyComment = studyCommentRepository.findAll().get(0);

        assertThat(studyComment.getId()).isNotNull();
    }

    @Test
    void 스터디_댓글_생성_실패__스터디의_맴버가_아님() {
        User fakeMember = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyCommentService.create(study.getId(), fakeMember.getId(), "message"));
    }

    @Test
    void 스터디_댓글_수정_성공() {
        studyComment = studyCommentRepository.save(createStudyComment(study, leader));

        studyCommentService.update(studyComment.getId(), leader.getId(), "updatedComment");

        assertThat(studyComment.getComment()).isEqualTo("updatedComment");
    }

    @Test
    void 스터디_댓글_수정_실패__작성자가_아님() {
        studyComment = studyCommentRepository.save(createStudyComment(study, leader));

        studyCommentService.create(study.getId(), leader.getId(), "message");

        User fakeMember = userRepository.save(createActivateUser("fakeMember"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyCommentService.update(studyComment.getId(), fakeMember.getId(), "updatedComment"));
    }

    @Test
    void 스터디_댓글_삭제_성공() {
        studyComment = studyCommentRepository.save(createStudyComment(study, leader));

        studyCommentService.create(study.getId(), leader.getId(), "message");

        studyCommentService.delete(studyComment.getId(), leader.getId());

        StudyComment foundStudyComment = studyCommentRepository.findAll().get(0);

        assertThat(foundStudyComment.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_댓글_삭제_실패__작성한_유저가_아님() {
        studyComment = studyCommentRepository.save(createStudyComment(study, leader));

        studyCommentService.create(study.getId(), leader.getId(), "message");

        User fakeMember = userRepository.save(createActivateUser("fakeMember"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyCommentService.delete(studyComment.getId(), fakeMember.getId()));
    }
}