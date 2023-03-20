package com.codelap.common.studyConfirmation.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyConfirmation.domain.StudyConfirmation;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.List;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
class StudyConfirmationDomainServiceTest {

    @Autowired
    private StudyConfirmationService studyConfirmationService;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyConfirmationRepository studyConfirmationRepository;

    private Study study;
    private User member;
    private User leader;
    private StudyConfirmationFile file;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));
        member = userRepository.save(User.create("user", 10, career, "abcd", "email"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));
        study.addMember(member);

        file = StudyConfirmationFile.create("saved", "original", 10L);
    }

    @Test
    void 스터디_인증_생성_성공() {
        studyConfirmationService.create(study.getId(), member.getId(), "title", "content", List.of(file));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        assertThat(studyConfirmation.getId()).isNotNull();
    }

    @Test
    void 스터디_인증_생성_실패__해당_유저가_아님() {
        UserCareer career = UserCareer.create("직무", 1);
        User fakeUser = userRepository.save(User.create("fakeUser", 10, career, "abcd", "fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() ->
                studyConfirmationService.create(study.getId(), fakeUser.getId(), "title", "content", List.of(file))
        );
    }
}