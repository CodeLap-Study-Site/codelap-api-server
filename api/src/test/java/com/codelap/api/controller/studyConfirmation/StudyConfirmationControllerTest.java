package com.codelap.api.controller.studyConfirmation;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyConfirmation.domain.StudyConfirmation;
import com.codelap.common.studyConfirmation.domain.StudyConfirmationRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationConfirmDto.StudyConfirmationConfirmRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequestFileDto;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationReConfirmDto.StudyConfirmationReConfirmRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationReConfirmDto.StudyConfirmationReConfirmRequestFileDto;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationRejectDto.StudyConfirmationRejectRequest;
import static com.codelap.api.support.HttpMethod.POST;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.*;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.support.TechStack.Spring;
import static com.codelap.fixture.StudyConfirmationFixture.createStudyConfirmation;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

class StudyConfirmationControllerTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyConfirmationRepository studyConfirmationRepository;

    private User leader;
    private User member;
    private Study study;
    private List<StudyTechStack> techStackList;


    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(createActivateUser());

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(new StudyTechStack(Java), new StudyTechStack(Spring));

        study = studyRepository.save(Study.create("팀", "정보", 4, HARD, period, needCareer, leader, techStackList));

        member = userRepository.save(createActivateUser());

        study.addMember(member);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_생성_성공() throws Exception {
        login(member);

        StudyConfirmationCreateRequestFileDto file = new StudyConfirmationCreateRequestFileDto("savedName", "originalName", 100L);
        StudyConfirmationCreateRequest req = new StudyConfirmationCreateRequest(study.getId(),"title", "contents", List.of(file));

        setMockMvcPerform(POST, req, "/study-confirmation", "study-confirmation/create");

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        assertThat(studyConfirmation.getId()).isNotNull();
        assertThat(studyConfirmation.getStudy()).isSameAs(study);
        assertThat(studyConfirmation.getUser()).isSameAs(member);
        assertThat(studyConfirmation.getTitle()).isEqualTo("title");
        assertThat(studyConfirmation.getContent()).isEqualTo("contents");
        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
        assertThat(studyConfirmation.getCreatedAt()).isNotNull();
    }

    @Test
    @WithUserDetails
    void 스터디_인증_확인_성공() throws Exception {
        login(leader);

        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        StudyConfirmationConfirmRequest req = new StudyConfirmationConfirmRequest(studyConfirmation.getId());

        setMockMvcPerform(POST, req, "/study-confirmation/confirm");

        assertThat(studyConfirmation.getStatus()).isEqualTo(CONFIRMED);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_거절_성공() throws Exception {
        login(leader);

        studyConfirmationRepository.save(createStudyConfirmation(study, member));

        StudyConfirmation studyConfirmation = studyConfirmationRepository.findAll().get(0);

        StudyConfirmationRejectRequest req = new StudyConfirmationRejectRequest(studyConfirmation.getId());

        setMockMvcPerform(POST, req, "/study-confirmation/reject");

        assertThat(studyConfirmation.getStatus()).isEqualTo(REJECTED);
    }

    @Test
    @WithUserDetails
    void 스터디_인증_재인증_성공() throws Exception {
        login(member);

        StudyConfirmation studyConfirmation = studyConfirmationRepository.save(createStudyConfirmation(study, member));

        studyConfirmation.setStatus(REJECTED);

        StudyConfirmationReConfirmRequestFileDto refile = new StudyConfirmationReConfirmRequestFileDto("savedName", "originalName", 100L);
        StudyConfirmationReConfirmRequest req = new StudyConfirmationReConfirmRequest(studyConfirmation.getId(), "title", "content", List.of(refile));

        setMockMvcPerform(POST, req, "/study-confirmation/reconfirm");

        assertThat(studyConfirmation.getStatus()).isEqualTo(CREATED);
    }
}