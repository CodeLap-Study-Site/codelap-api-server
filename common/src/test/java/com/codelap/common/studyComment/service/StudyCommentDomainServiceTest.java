package com.codelap.common.studyComment.service;

import com.codelap.common.study.domain.*;
import com.codelap.common.studyComment.domain.StudyComment;
import com.codelap.common.studyComment.domain.StudyCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
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
    private List<TechStack> techStackList;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(Java, Spring);

        study = Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
        study = studyRepository.save(study);
    }

    @Test
    void 스터디_댓글_생성_성공() {
        studyCommentService.create(study.getId(), leader.getId(), "message");

        StudyComment studyComment = studyCommentRepository.findAll().get(0);

        assertThat(studyComment.getId()).isNotNull();
    }

    @Test
    void 스터디_댓글_생성_실패__스터디의_맴버가_아님() {
        UserCareer career = UserCareer.create("직무", 1);
        User fakeMember = userRepository.save(User.create("fakeMember", 10, career, "abcd", "fakeMember"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyCommentService.create(study.getId(), fakeMember.getId(), "message"));
    }

    @Test
    void 스터디_댓글_수정_성공(){
        StudyComment studyComment = studyCommentService.create(study.getId(), leader.getId(), "message");

        studyCommentService.update(studyComment.getId(), leader.getId(), "updatedComment");

        StudyComment foundstudyComment = studyCommentRepository.findAll().get(0);

        assertThat(foundstudyComment.getComment()).isEqualTo("updatedComment");
    }

    @Test
    void 스터디_댓글_수정_실패__작성자가_아님(){
        StudyComment studyComment = studyCommentService.create(study.getId(), leader.getId(), "message");

        UserCareer career = UserCareer.create("직무", 1);
        User fakeMember = userRepository.save(User.create("fakeMember", 10, career, "abcd", "fakeMember"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyCommentService.update(studyComment.getId(), fakeMember.getId(), "updatedComment"));
    }
}