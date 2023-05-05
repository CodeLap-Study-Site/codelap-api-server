package com.codelap.common.studyConfirmation.service;

import com.codelap.common.study.domain.*;
import com.codelap.common.studyConfirmation.domain.StudyConfirmation;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.fixture.StudyConfirmationFixture;
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
import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.*;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static com.codelap.fixture.StudyConfirmationFixture.*;
import static com.codelap.fixture.StudyConfirmationFixture.createStudyConfirmation;
import static com.codelap.fixture.StudyFixture.*;
import static com.codelap.fixture.UserFixture.createActivateUser;
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

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser("leader"));
        member = userRepository.save(createActivateUser("member"));

        study = studyRepository.save(createStudy(leader));

        study.addMember(member);
    }

    @Test
    void 스터디_인증_생성_성공() {
        StudyConfirmationFile file = StudyConfirmationFile.create("saved", "original", 10L);

        studyConfirmationService.create(study.getId(), member.getId(), "title", "content", List.of(file));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        assertThat(studyConfirmation.getId()).isNotNull();
    }

    @Test
    void 스터디_인증_생성_실패__해당_유저가_아님() {
        User fakeUser = userRepository.save(createActivateUser("fakeUser"));

        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        assertThatActorValidateCodeLapException().isThrownBy(() ->
                studyConfirmationService.create(study.getId(), fakeUser.getId(), "title", "content", files)
        );
    }

    @Test
    void 스터디_인증_확인_성공() {
        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        studyConfirmationService.confirm(studyConfirmation.getId(), leader.getId());

        assertThat(studyConfirmation.getStatus()).isEqualTo(CONFIRMED);
    }

    @Test
    void 스터디_인증_확인_실패__리더가_아님() {
        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        assertThatActorValidateCodeLapException().isThrownBy(() ->
                studyConfirmationService.confirm(studyConfirmation.getId(), member.getId())
        );
    }

    @Test
    void 스터디_인증_거절_성공() {
        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        studyConfirmationService.reject(studyConfirmation.getId(), leader.getId());

        assertThat(studyConfirmation.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    void 스터디_인증_거절_실패__리더가_아님() {
        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        assertThatActorValidateCodeLapException().isThrownBy(() ->
                studyConfirmationService.reject(studyConfirmation.getId(), member.getId())
        );
    }

    @Test
    void 스터디_인증_재인증_성공() {
        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        studyConfirmation.setStatus(REJECTED);

        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        studyConfirmationService.reConfirm(studyConfirmation.getId(), member.getId(), "title", "content", files);

        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
    }

    @Test
    void 스터디_인증_재인증_실패__사용자가_인증의_주인이_아님() {
        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        User fakeUser = userRepository.save(createActivateUser("fakeUser"));

        List<StudyConfirmationFile> files = createStudyConfirmationFiles();

        assertThatActorValidateCodeLapException().isThrownBy(() ->
                studyConfirmationService.reConfirm(studyConfirmation.getId(), fakeUser.getId(), "title", "content", files)
        );
    }

    @Test
    void 스터디_인증_취소_성공() {
        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        studyConfirmationService.delete(studyConfirmation.getId(), member.getId());

        assertThat(studyConfirmation.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_인증_취소_실패__사용자가_인증의_주인이_아님() {
        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        User fakeUser = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() ->
                studyConfirmationService.delete(studyConfirmation.getId(), fakeUser.getId())
        );
    }
}