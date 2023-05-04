package com.codelap.common.studyNoticeComment.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyTechStack;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.common.study.domain.Study.create;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.DELETED;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
public class StudyNoticeCommentDomainServiceTest {


    @Autowired
    private StudyNoticeCommentService studyNoticeCommentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyNoticeRepository studyNoticeRepository;

    @Autowired
    private StudyNoticeCommentRepository studyNoticeCommentRepository;

    private StudyNotice studyNotice;
    private Study study;
    private User leader;
    private User member;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser("leader"));
        member = userRepository.save(createActivateUser("member"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        List<StudyTechStack> techStackList = Arrays.asList(new StudyTechStack(Java), new StudyTechStack(Spring));

        study = create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);

        study.addMember(member);

        StudyNoticeFile file = StudyNoticeFile.create("savedName", "originalName", 100L);

        studyNotice = studyNoticeRepository.save(StudyNotice.create(study, "title", "contents", List.of(file)));
    }

    @Test
    void 스터디_공지_댓글_생성_성공() {
        StudyNoticeComment studyNoticeComment = studyNoticeCommentService.create(studyNotice.getId(), member.getId(), "content");

        studyNoticeComment = studyNoticeCommentRepository.findById(studyNoticeComment.getId()).orElseThrow();

        assertThat(studyNoticeComment.getId()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_댓글_생성_실패__댓글_내용이_널이거나_공백(String content) {
        assertThatIllegalArgumentException().isThrownBy(() -> studyNoticeCommentService.create(studyNotice.getId(), member.getId(), content));
    }

    @Test
    void 스터디_공지_댓글_삭제_성공() {
        StudyNoticeComment studyNoticeComment = studyNoticeCommentService.create(studyNotice.getId(), member.getId(), "content");

        studyNoticeCommentService.delete(studyNoticeComment.getId(), member.getId());

        studyNoticeComment = studyNoticeCommentRepository.findById(studyNoticeComment.getId()).orElseThrow();

        assertThat(studyNoticeComment.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_공지_댓글_삭제_실패__작성한_유저가_아님() {
        StudyNoticeComment studyNoticeComment = studyNoticeCommentService.create(studyNotice.getId(), member.getId(), "content");

        User fakeUser = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeCommentService.delete(studyNoticeComment.getId(), fakeUser.getId()));
    }

    @Test
    void 스터디_공지_댓글_수정_성공() {
        StudyNoticeComment studyNoticeComment = studyNoticeCommentService.create(studyNotice.getId(), member.getId(), "content");

        studyNoticeCommentService.update(studyNoticeComment.getId(), member.getId(), "content");

        assertThat(studyNoticeComment.getContent()).isEqualTo("content");
    }

    @Test
    void 스터디_공지_댓글_수정_실패__작성한_유저가_아님() {
        StudyNoticeComment studyNoticeComment = studyNoticeCommentService.create(studyNotice.getId(), member.getId(), "content");

        User fakeUser = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeCommentService.update(studyNoticeComment.getId(), fakeUser.getId(), "content"));
    }
}
