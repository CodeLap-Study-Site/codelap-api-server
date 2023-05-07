package com.codelap.common.studyNotice.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.DELETED;
import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyNoticeFixture.createStudyNotice;
import static com.codelap.fixture.StudyNoticeFixture.createStudyNoticeFiles;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class StudyNoticeDomainServiceTest {

    @Autowired
    private StudyNoticeService studyNoticeService;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyNoticeRepository studyNoticeRepository;

    private User leader;
    private Study study;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());

        study = studyRepository.save(createStudy(leader));
    }

    @Test
    void 스터디_공지_생성_성공() {
        List<StudyNoticeFile> files = createStudyNoticeFiles();

        studyNoticeService.create(study.getId(), leader.getId(), "title", "contents", files);

        StudyNotice studyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(studyNotice.getId()).isNotNull();
        assertThat(studyNotice.getTitle()).isEqualTo("title");
        assertThat(studyNotice.getContents()).isEqualTo("contents");
        assertThat(studyNotice.getFiles()).isNotNull();
    }

    @Test
    void 스터디_공지_생성_실패__리더가_아님() {
        User fakeLeader = userRepository.save(createActivateUser("fakeUser"));

        List<StudyNoticeFile> files = createStudyNoticeFiles();

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeService.create(study.getId(), fakeLeader.getId(), "title", "contents", files));
    }

    @Test
    void 스터디_공지_수정_성공() {
        StudyNotice studyNotice = studyNoticeRepository.save(createStudyNotice(study));

        List<StudyNoticeFile> files = createStudyNoticeFiles();

        studyNoticeService.update(studyNotice.getId(), leader.getId(), "updateTitle", "updatedContents", files);

        StudyNotice foundStudyNotice = studyNoticeRepository.findById(studyNotice.getId()).orElseThrow();

        assertThat(foundStudyNotice.getTitle()).isEqualTo("updateTitle");
        assertThat(foundStudyNotice.getContents()).isEqualTo("updatedContents");
        assertThat(foundStudyNotice.getFiles()).isNotNull();
    }

    @Test
    void 스터디_공지_수정_실패__리더가_아님() {
        StudyNotice studyNotice = studyNoticeRepository.save(createStudyNotice(study));

        List<StudyNoticeFile> files = createStudyNoticeFiles();

        User fakeLeader = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeService.update(studyNotice.getId(), fakeLeader.getId(), "updateTitle", "updatedContents", files));
    }

    @Test
    void 스터디_공지_삭제_성공() {
        StudyNotice studyNotice = studyNoticeRepository.save(createStudyNotice(study));

        studyNoticeService.delete(studyNotice.getId(), leader.getId());

        StudyNotice foundStudyNotice = studyNoticeRepository.findById(studyNotice.getId()).orElseThrow();

        assertThat(foundStudyNotice.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_공지_삭제_실패__리더가_아님() {
        StudyNotice studyNotice = studyNoticeRepository.save(createStudyNotice(study));

        User fakeLeader = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> studyNoticeService.delete(studyNotice.getId(), fakeLeader.getId()));
    }
}