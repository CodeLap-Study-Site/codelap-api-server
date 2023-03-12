package com.codelap.api.controller.study;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;

import static com.codelap.api.controller.study.dto.StudyCreateDto.*;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyControllerTest extends ApiTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;
    private User leader;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));
    }

    @Test
    void 스터디_생성_성공() throws Exception {
        StudyCreateRequestStudyPeriodDto periodDto = new StudyCreateRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyCreateRequestStudyNeedCareerDto careerDto = new StudyCreateRequestStudyNeedCareerDto("직무", 10);

        StudyCreateRequest req = new StudyCreateRequest(leader.getId(), "팀", "정보", 4, HARD, periodDto, careerDto);

        mockMvc.perform(post("/study")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findAll().get(0);

        assertThat(foundStudy.getId()).isNotNull();
        assertThat(foundStudy.getName()).isEqualTo("팀");
        assertThat(foundStudy.getInfo()).isEqualTo("정보");
        assertThat(foundStudy.getMaxMembersSize()).isEqualTo(4);
        assertThat(foundStudy.getDifficulty()).isEqualTo(HARD);
        assertThat(foundStudy.getPeriod().getStartAt()).isNotNull();
        assertThat(foundStudy.getPeriod().getEndAt()).isNotNull();
        assertThat(foundStudy.getNeedCareer().getOccupation()).isEqualTo("직무");
        assertThat(foundStudy.getNeedCareer().getYear()).isEqualTo(10);
        assertThat(foundStudy.getStatus()).isEqualTo(OPENED);
        assertThat(foundStudy.getCreatedAt()).isNotNull();
        assertThat(foundStudy.getLeader()).isSameAs(leader);
        assertThat(foundStudy.getMembers()).containsExactly(leader);
    }
}