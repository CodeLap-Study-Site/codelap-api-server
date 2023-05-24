package com.codelap.api.controller.studyNotice;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.codelap.api.controller.studyNotice.dto.StudyNoticeCreateDto.StudyNoticeCreateRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeCreateDto.StudyNoticeCreateRequestFileDto;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeDeleteDto.StudyNoticeDeleteRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeUpdateDto.StudyNoticeUpdateRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeUpdateDto.StudyNoticeUpdateRequestFileDto;
import static com.codelap.api.support.HttpMethod.DELETE;
import static com.codelap.api.support.HttpMethod.POST;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.CREATED;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.DELETED;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.StudyNoticeFixture.createStudyNotice;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

class StudyNoticeControllerTest extends ApiTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private StudyNoticeRepository studyNoticeRepository;

    private User leader;
    private Study study;
    private StudyNotice studyNotice;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
        study = studyRepository.save(createStudy(leader));
        studyNotice = studyNoticeRepository.save(createStudyNotice(study));
    }

    @Test
    @WithUserDetails
    void 스터디_공지_생성_성공() throws Exception {
        login(leader);

        StudyNoticeCreateRequestFileDto file = new StudyNoticeCreateRequestFileDto("s3ImageURL", "originalName");
        StudyNoticeCreateRequest req = new StudyNoticeCreateRequest(study.getId(), "title", "contents", List.of(file));

        setMockMvcPerform(POST, req, "/study-notice", "study-notice/create");

        StudyNotice studyNotice = studyNoticeRepository.findAll().get(1);

        assertThat(studyNotice.getId()).isNotNull();
        assertThat(studyNotice.getStudy()).isSameAs(study);
        assertThat(studyNotice.getTitle()).isEqualTo(req.title());
        assertThat(studyNotice.getContents()).isEqualTo(req.contents());
        assertThat(studyNotice.getCreatedAt()).isNotNull();
        assertThat(studyNotice.getStatus()).isEqualTo(CREATED);
    }

    @Test
    @WithUserDetails
    void 스터디_공지_수정_성공() throws Exception {
        login(leader);

        StudyNoticeUpdateRequestFileDto file = new StudyNoticeUpdateRequestFileDto("s3ImageURL", "originalName");
        StudyNoticeUpdateRequest req = new StudyNoticeUpdateRequest(studyNotice.getId(), "title", "contents", List.of(file));

        setMockMvcPerform(POST, req, "/study-notice/update");

        StudyNotice foundStudyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(foundStudyNotice.getTitle()).isEqualTo(req.title());
        assertThat(foundStudyNotice.getContents()).isEqualTo(req.contents());
        assertThat(foundStudyNotice.getCreatedAt()).isNotNull();
        assertThat(foundStudyNotice.getStatus()).isEqualTo(CREATED);
    }

    @Test
    @WithUserDetails
    void 스터디_공지_삭제_성공() throws Exception {
        login(leader);

        StudyNoticeDeleteRequest req = new StudyNoticeDeleteRequest(studyNotice.getId());

        setMockMvcPerform(DELETE, req, "/study-notice", "study-notice/delete");

        StudyNotice studyNotice = studyNoticeRepository.findAll().get(0);

        assertThat(studyNotice.getStatus()).isEqualTo(DELETED);
    }
}