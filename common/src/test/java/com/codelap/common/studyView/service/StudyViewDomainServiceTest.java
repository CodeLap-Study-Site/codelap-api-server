package com.codelap.common.studyView.service;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyView.domain.StudyView;
import com.codelap.common.studyView.domain.StudyViewRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class StudyViewDomainServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyViewRepository studyViewRepository;

    @Autowired
    private StudyViewService studyViewService;


    private User leader;
    private Study study;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());

        study = studyRepository.save(createStudy(leader));
    }

    @Test
    void 스터디_조회수_생성_성공() {
        studyViewService.create(study.getId(), "1.1.1.1");

        StudyView studyView = studyViewRepository.findAll().get(0);

        assertThat(studyView.getId()).isNotNull();
    }

    @Test
    void 스터디_조회수_생성_실패__동일한_IP() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            for (int i = 0; i < 2; i++) {
                studyViewService.create(study.getId(), "1.1.1.1");
            }
        });
    }
}