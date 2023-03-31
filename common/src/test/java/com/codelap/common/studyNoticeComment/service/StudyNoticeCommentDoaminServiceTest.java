package com.codelap.common.studyNoticeComment.service;

import com.codelap.common.study.domain.*;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.studyNotice.service.StudyNoticeService;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeComment;
import com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
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
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
public class StudyNoticeCommentDoaminServiceTest {

    @Autowired
    private StudyNoticeCommentService studyNoticeCommentService;

    @Autowired
    private StudyRepository studyRepository;
    private StudyNoticeService studyNoticeService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyNoticeCommentRepository studyNoticeCommentRepository;

    private StudyNotice studyNotice;
    private User leader;

    private User member;

    private Study study;

    private StudyNoticeFile file;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = User.create("name", 10, career, "abcd", "setUp");
        member = userRepository.save(User.create("user", 10, career, "abcd", "email"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        List<TechStack> techStackList = Arrays.asList(Java, Spring);

        Study study = create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList);
        StudyNoticeFile file = StudyNoticeFile.create("savedName", "originalName", 100L);
        study.addMember(member);

        studyNotice = StudyNotice.create(study, "title", "contents", List.of(file));
    }

    @Test
    void 스터디_공지_댓글_생성_성공() {
        studyNoticeCommentService.create(studyNotice, member.getId(), "content");

        StudyNoticeComment studyNoticeComment = studyNoticeCommentRepository.findAll().get(0);

        assertThat(studyNoticeComment.getId()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 스터디_공지_댓글_생성_실패__댓글_내용이_널이거나_공백(String content){
        assertThatIllegalArgumentException().isThrownBy(() -> studyNoticeCommentService.create(studyNotice, member.getId(), content));
    }
}
