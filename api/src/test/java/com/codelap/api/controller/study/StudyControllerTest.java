package com.codelap.api.controller.study;

import com.codelap.api.support.ApiTest;
import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.domain.StudyTechStack;
import com.codelap.common.study.service.StudyService;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyView.service.StudyViewService;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.OffsetDateTime;
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
import static com.codelap.api.support.HttpMethod.*;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.StudyStatus.*;
import static com.codelap.common.support.TechStack.*;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static com.codelap.fixture.UserFixture.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class StudyControllerTest extends ApiTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyService studyService;

    @Autowired
    StudyCommentService studyCommentService;

    @Autowired
    StudyViewService studyViewService;

    @Autowired
    BookmarkService bookmarkService;

    private User leader;
    private User member;
    private Study study;


    @BeforeEach
    void setUp() {
        leader = prepareLoggedInUser();
        member = userRepository.save(createActivateUser("member"));
        study = studyRepository.save(createStudy(leader));
    }

    @Test
    @WithUserDetails
    void 스터디_생성_성공() throws Exception {
        StudyTechStack studyTechStack = new StudyTechStack(Java);
        StudyCreateRequestStudyPeriodDto periodDto = new StudyCreateRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyCreateRequestStudyNeedCareerDto careerDto = new StudyCreateRequestStudyNeedCareerDto("직무", 10);

        StudyCreateRequest req = new StudyCreateRequest("팀", "정보", 4, HARD, periodDto, careerDto, List.of(studyTechStack));

        setMockMvcPerform(POST, req, "/study", "study/create");

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
    @WithUserDetails
    void 스터디_수정_성공() throws Exception {
        StudyTechStack studyTechStack = new StudyTechStack(Java);
        StudyUpdateRequestStudyPeriodDto periodDto = new StudyUpdateRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyUpdateRequestStudyNeedCareerDto careerDto = new StudyUpdateRequestStudyNeedCareerDto("updateOccupation", 5);

        StudyUpdateRequest req = new StudyUpdateRequest(study.getId(), "updateTeam", "updateInfo", 5, HARD, periodDto, careerDto, List.of(studyTechStack));

        setMockMvcPerform(POST, req, "/study/update");

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
    @WithUserDetails
    void 스터디_진행_성공() throws Exception {
        StudyProceedRequest req = new StudyProceedRequest(study.getId());

        setMockMvcPerform(POST, req, "/study/proceed");

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    @WithUserDetails
    void 스터디_멤버_추방_성공() throws Exception {
        member = userRepository.save(createUser());

        study.addMember(member);

        StudyRemoveMemberRequest req = new StudyRemoveMemberRequest(study.getId(), member.getId());

        setMockMvcPerform(POST, req, "/study/remove-member");

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getMembers()).doesNotContain(member);
    }

    @Test
    @WithUserDetails
    void 스터디_닫기_성공() throws Exception {
        StudyCloseRequest req = new StudyCloseRequest(study.getId());

        setMockMvcPerform(POST, req, "/study/close");

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    @WithUserDetails
    void 스터디_나가기_성공() throws Exception {
        member = prepareLoggedInUser();
        study.addMember(member);

        StudyLeaveRequest req = new StudyLeaveRequest(study.getId(), member.getId());

        setMockMvcPerform(POST, req, "/study/leave");

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getMembers()).doesNotContain(member);
    }

    @Test
    @WithUserDetails
    void 스터디_삭제_성공() throws Exception {
        StudyDeleteRequest req = new StudyDeleteRequest(study.getId());

        setMockMvcPerform(DELETE, req, "/study/delete");

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(DELETED);
    }

    @Test
    @WithUserDetails
    void 스터디_오픈_성공() throws Exception {
        study.proceed();

        StudyOpenRequestStudyPeriodDto periodDto = new StudyOpenRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyOpenRequest req = new StudyOpenRequest(study.getId(), leader.getId(), periodDto);

        setMockMvcPerform(POST, req, "/study/open");

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(OPENED);
    }

    @Test
    @WithUserDetails
    void 유저가_참여한_스터디_조회_성공() throws Exception {
        User leader = prepareLoggedInActiveUser();

        유저가_참여한_스터디_조회_스터디_생성(leader);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("userId", member.getId().toString());
        params.add("statusCond", "open");
        params.add("techStackList", "Spring");
        params.add("techStackList", "AWS");

        setMockMvcPerform(GET, params, 유저가_참여한_스터디_조회_검증(params), "/study/my-study");
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        Study study1 = studyRepository.save(createStudy(leader, Java));
        study1.addMember(member);

        studyCommentService.create(study1.getId(), member.getId(), "message");
        studyViewService.create(study1.getId(), "1.1.1.1");
        bookmarkService.create(study1.getId(), member.getId());

        Study study2 = studyRepository.save(createStudy(leader, React));
        study2.addMember(member);

        studyCommentService.create(study2.getId(), member.getId(), "message");
        studyViewService.create(study2.getId(), "1.1.1.2");

        Study study3 = studyRepository.save(createStudy(leader, AWS));
        study3.addMember(member);

        studyService.close(study3.getId(), leader.getId());

        studyRepository.save(createStudy(leader, ReactNative));
    }

    private ResultMatcher[] 유저가_참여한_스터디_조회_검증(MultiValueMap<String, String> params) {
        List<Study> studiesContainsMember = studyRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() != DELETED && it.getStatus() != CLOSED && it.containsMember(member))
                .filter(study -> study.getTechStackList()
                        .stream()
                        .anyMatch(techStack -> params.get("techStackList").stream()
                                .anyMatch(stack -> stack.equals(techStack.getTechStack().toString()))))
                .collect(Collectors.toList());

        return IntStream.range(0, studiesContainsMember.size())
                .mapToObj(index -> {
                    Study indexStudy = studiesContainsMember.get(index);

                    List<String> techStackList = indexStudy.getTechStackList()
                            .stream()
                            .map(StudyTechStack::getTechStack)
                            .map(Enum::name)
                            .collect(Collectors.toList());

                    return Map.entry(index, List.of(
                            jsonPath("$.studies.[" + index + "].studyName").value(indexStudy.getName()),
                            jsonPath("$.studies.[" + index + "].studyPeriod").isNotEmpty(),
                            jsonPath("$.studies.[" + index + "].leaderName").value(indexStudy.getLeader().getName()),
                            jsonPath("$.studies.[" + index + "].commentCount").value(indexStudy.getComments().size()),
                            jsonPath("$.studies.[" + index + "].viewCount").value(indexStudy.getViews().size()),
                            jsonPath("$.studies.[" + index + "].bookmarkCount").value(indexStudy.getBookmarks().size()),
                            jsonPath("$.studies.[" + index + "].maxMemberSize").value(indexStudy.getMaxMembersSize()),
                            jsonPath("$.studies.[" + index + "].techStackList[*].techStack").value(techStackList)
                    ));
                })
                .flatMap(entry -> entry.getValue().stream())
                .toArray(ResultMatcher[]::new);
    }
}
