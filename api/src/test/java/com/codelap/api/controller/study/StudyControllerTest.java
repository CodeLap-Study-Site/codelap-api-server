package com.codelap.api.controller.study;

import com.codelap.api.support.ApiTest;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;

import static com.codelap.api.controller.study.dto.StudyCloseDto.StudyCloseRequest;
import static com.codelap.api.controller.study.dto.StudyCreateDto.*;
import static com.codelap.api.controller.study.dto.StudyLeaveDto.StudyLeaveRequest;
import static com.codelap.api.controller.study.dto.StudyProceedDto.StudyProceedRequest;
import static com.codelap.api.controller.study.dto.StudyRemoveMemberDto.StudyRemoveMemberRequest;
import static com.codelap.api.controller.study.dto.StudyUpdateDto.*;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.*;
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

    @Test
    void 스터디_수정_성공() throws Exception {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        Study study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));

        StudyUpdateRequestStudyPeriodDto periodDto = new StudyUpdateRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyUpdateRequestStudyNeedCareerDto careerDto = new StudyUpdateRequestStudyNeedCareerDto("updateOccupation", 5);

        StudyUpdateRequest req = new StudyUpdateRequest(leader.getId(), study.getId(), "updateTeam", "updateInfo", 5, HARD, periodDto, careerDto);

        mockMvc.perform(post("/study/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findAll().get(0);

        assertThat(foundStudy.getId()).isNotNull();
        assertThat(foundStudy.getName()).isEqualTo("updateTeam");
        assertThat(foundStudy.getInfo()).isEqualTo("updateInfo");
        assertThat(foundStudy.getMaxMembersSize()).isEqualTo(5);
        assertThat(foundStudy.getDifficulty()).isEqualTo(HARD);
        assertThat(foundStudy.getPeriod().getStartAt()).isNotNull();
        assertThat(foundStudy.getPeriod().getEndAt()).isNotNull();
        assertThat(foundStudy.getNeedCareer().getOccupation()).isEqualTo("updateOccupation");
        assertThat(foundStudy.getNeedCareer().getYear()).isEqualTo(5);
        assertThat(foundStudy.getStatus()).isEqualTo(OPENED);
        assertThat(foundStudy.getCreatedAt()).isNotNull();
        assertThat(foundStudy.getLeader()).isSameAs(leader);
        assertThat(foundStudy.getMembers()).containsExactly(leader);
    }

    @Test
    void 스터디_진행_성공() throws Exception {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        Study study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));

        StudyProceedRequest req = new StudyProceedRequest(study.getId(), leader.getId());

        mockMvc.perform(post("/study/proceed")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/proceed",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    void 스터디_멤버_추방_성공() throws Exception {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        Study study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));

        UserCareer career = UserCareer.create("직무", 1);
        User member = userRepository.save(User.create("member", 10, career, "abcd", "member"));

        study.addMember(member);

        StudyRemoveMemberRequest req = new StudyRemoveMemberRequest(study.getId(), member.getId(), leader.getId());

        mockMvc.perform(post("/study/remove-member")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/remove-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getMembers()).doesNotContain(member);
    }

    @Test
    void 스터디_닫기_성공() throws Exception {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        Study study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));

        StudyCloseRequest req = new StudyCloseRequest(study.getId(), leader.getId());

        mockMvc.perform(post("/study/close")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/close",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    void 스터디_나가기_성공() throws Exception {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        Study study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader));

        UserCareer career = UserCareer.create("직무", 1);
        User member = userRepository.save(User.create("member", 10, career, "abcd", "member"));

        study.addMember(member);

        StudyLeaveRequest req = new StudyLeaveRequest(study.getId(), member.getId());

        mockMvc.perform(post("/study/leave")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/leave",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getMembers()).doesNotContain(member);
    }
}