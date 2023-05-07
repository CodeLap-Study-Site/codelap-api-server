package com.codelap.common.studyNoticeComment.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.fixture.StudyNoticeFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.DELETED;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyNoticeCommentFixture.createStudyNoticeComment;
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

    @Autowired
    private StudyRepository studyRepository;

    private StudyNotice studyNotice;
    private Study study;
    private User leader;
    private User member;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser("leader"));
        member = userRepository.save(createActivateUser("member"));

        study = studyRepository.save(createStudy(leader));

        study.addMember(member);

        studyNotice = studyNoticeRepository.save(StudyNoticeFixture.createStudyNotice(study));
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
        StudyNoticeComment studyNoticeComment = studyNoticeCommentRepository.save(createStudyNoticeComment(studyNotice, member));

        studyNoticeCommentService.delete(studyNoticeComment.getId(), member.getId());

        studyNoticeComment = studyNoticeCommentRepository.findById(studyNoticeComment.getId()).orElseThrow();

        assertThat(studyNoticeComment.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_공지_댓글_삭제_실패__작성한_유저가_아님() {
        StudyNoticeComment studyNoticeComment = studyNoticeCommentRepository.save(createStudyNoticeComment(studyNotice, member));

        User fakeUser = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeCommentService.delete(studyNoticeComment.getId(), fakeUser.getId()));
    }

    @Test
    void 스터디_공지_댓글_수정_성공() {
        StudyNoticeComment studyNoticeComment = studyNoticeCommentRepository.save(createStudyNoticeComment(studyNotice, member));

        studyNoticeCommentService.update(studyNoticeComment.getId(), member.getId(), "content");

        assertThat(studyNoticeComment.getContent()).isEqualTo("content");
    }

    @Test
    void 스터디_공지_댓글_수정_실패__작성한_유저가_아님() {
        StudyNoticeComment studyNoticeComment = studyNoticeCommentRepository.save(createStudyNoticeComment(studyNotice, member));

        User fakeUser = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeCommentService.update(studyNoticeComment.getId(), fakeUser.getId(), "content"));
    }
}
