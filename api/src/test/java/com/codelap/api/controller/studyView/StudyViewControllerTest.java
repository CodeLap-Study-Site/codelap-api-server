package com.codelap.api.controller.studyView;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyView.domain.StudyView;
import com.codelap.common.studyView.domain.StudyViewRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static com.codelap.api.controller.studyView.dto.StudyViewCreateDto.StudyViewCreateRequest;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class StudyViewControllerTest extends ApiTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyViewRepository studyViewRepository;

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

        study = studyRepository.save(Study.create("팀", "정보", 4, HARD, period, needCareer, leader, techStackList));
    }

    @Test
    void 스터디_조회수_생성_성공() throws Exception {
        StudyViewCreateRequest req = new StudyViewCreateRequest(study.getId());

        mockMvc.perform(post("/study-view")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study-view/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        StudyView studyView = studyViewRepository.findAll().get(0);

        assertThat(studyView.getId()).isNotNull();
    }
}