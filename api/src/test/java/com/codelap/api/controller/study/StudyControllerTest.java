package com.codelap.api.controller.study;

import com.codelap.api.support.ApiTest;
import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyView.service.StudyViewService;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codelap.api.controller.study.dto.StudyCloseDto.StudyCloseRequest;
import static com.codelap.api.controller.study.dto.StudyCreateDto.*;
import static com.codelap.api.controller.study.dto.StudyDeleteDto.StudyDeleteRequest;
import static com.codelap.api.controller.study.dto.StudyLeaveDto.StudyLeaveRequest;
import static com.codelap.api.controller.study.dto.StudyOpenDto.StudyOpenRequest;
import static com.codelap.api.controller.study.dto.StudyOpenDto.StudyOpenRequestStudyPeriodDto;
import static com.codelap.api.controller.study.dto.StudyProceedDto.StudyProceedRequest;
import static com.codelap.api.controller.study.dto.StudyRemoveMemberDto.StudyRemoveMemberRequest;
import static com.codelap.api.controller.study.dto.StudyUpdateDto.*;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.*;
import static com.codelap.common.study.domain.TechStack.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyControllerTest extends ApiTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyCommentService studyCommentService;

    @Autowired
    StudyViewService studyViewService;

    @Autowired
    BookmarkService bookmarkService;

    private User leader;
    private User member;
    private Study study;
    private List<TechStack> techStackList;
    private Study study1;
    private Study study2;


    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);
        techStackList = Arrays.asList(Java, Spring);

        study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, techStackList));
    }

    @Test
    void 스터디_생성_성공() throws Exception {
        StudyCreateRequestStudyPeriodDto periodDto = new StudyCreateRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyCreateRequestStudyNeedCareerDto careerDto = new StudyCreateRequestStudyNeedCareerDto("직무", 10);

        StudyCreateRequest req = new StudyCreateRequest(leader.getId(), "팀", "정보", 4, HARD, periodDto, careerDto, techStackList);

        mockMvc.perform(post("/study")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findAll().get(1);

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
        StudyUpdateRequestStudyPeriodDto periodDto = new StudyUpdateRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyUpdateRequestStudyNeedCareerDto careerDto = new StudyUpdateRequestStudyNeedCareerDto("updateOccupation", 5);

        StudyUpdateRequest req = new StudyUpdateRequest(study.getId(), leader.getId(), "updateTeam", "updateInfo", 5, HARD, periodDto, careerDto, techStackList);

        mockMvc.perform(post("/study/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

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

    @Test
    void 스터디_삭제_성공() throws Exception {
        StudyDeleteRequest req = new StudyDeleteRequest(study.getId(), leader.getId());

        mockMvc.perform(delete("/study/delete")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 스터디_오픈_성공() throws Exception {
        study.proceed();

        StudyOpenRequestStudyPeriodDto periodDto = new StudyOpenRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyOpenRequest req = new StudyOpenRequest(study.getId(), leader.getId(), periodDto);

        mockMvc.perform(post("/study/open")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("study/open",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(OPENED);
    }

    @Test
    void 유저가_참여한_스터디_조회_성공() throws Exception {
        UserCareer career = UserCareer.create("직무", 1);
        User leader = userRepository.save(User.create("name", 10, career, "abcd", "leader"));

        member = userRepository.save(User.create("member", 10, career, "abcd", "email"));

        유저가_참여한_스터디_조회_스터디_생성(leader);

        mockMvc.perform(get("/study")
                        .param("userId", member.getId().toString()))
                .andExpect(status().isOk())
                .andExpectAll(유저가_참여한_스터디_조회_검증())
                .andDo(document("study/my-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        techStackList = List.of(Spring, Java);

        study1 = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, List.of(Spring, Java)));

        study1.addMember(member);

        studyCommentService.create(study1.getId(), member.getId(), "message");
        studyViewService.create(study1.getId(), "1.1.1.1");
        bookmarkService.create(study1.getId(), member.getId());

        study2 = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, List.of(JavaScript, React)));

        study2.addMember(member);

        studyCommentService.create(study2.getId(), member.getId(), "message");
        studyViewService.create(study2.getId(), "1.1.1.2");
    }

    private ResultMatcher[] 유저가_참여한_스터디_조회_검증() {
        List<Study> studiesContainsMember = studyRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() != DELETED)
                .filter(it -> it.containsMember(member))
                .collect(Collectors.toList());

        return IntStream.range(0, studiesContainsMember.size())
                .mapToObj(index -> {
                    Study indexStudy = studiesContainsMember.get(index);

                    return Map.entry(index, List.of(
                            jsonPath("$.studies.[" + index + "].studyName").value(indexStudy.getName()),
                            jsonPath("$.studies.[" + index + "].studyPeriod").isNotEmpty(),
                            jsonPath("$.studies.[" + index + "].leaderName").value(indexStudy.getLeader().getName()),
                            jsonPath("$.studies.[" + index + "].commentCount").value(indexStudy.getComments().size()),
                            jsonPath("$.studies.[" + index + "].viewCount").value(indexStudy.getViews().size()),
                            jsonPath("$.studies.[" + index + "].bookmarkCount").value(indexStudy.getBookmarks().size()),
                            jsonPath("$.studies.[" + index + "].maxMemberSize").value(indexStudy.getMaxMembersSize()),
                            jsonPath("$.studies.[" + index + "].techStackList.[" + index + "]").value(indexStudy.getTechStackList().get(index).toString())
                    ));
                })
                .flatMap(entry -> entry.getValue().stream())
                .toArray(ResultMatcher[]::new);
    }
}
