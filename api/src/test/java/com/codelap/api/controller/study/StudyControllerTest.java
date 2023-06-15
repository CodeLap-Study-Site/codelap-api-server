package com.codelap.api.controller.study;

import com.codelap.api.controller.study.cond.GetBookmarkStudyCardsCond;
import com.codelap.api.controller.study.cond.GetStudyCardsCond.GetStudyCardsParam;
import com.codelap.api.support.ApiTest;
import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
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
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
import static com.codelap.api.support.RestDocumentationUtils.*;
import static com.codelap.common.study.domain.StudyDifficulty.HARD;
import static com.codelap.common.study.domain.StudyStatus.*;
import static com.codelap.common.support.TechStack.*;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StudyControllerTest extends ApiTest {

    private static final String DOCS_TAG = "Study";

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

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

    private Bookmark bookmark;


    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser("leader"));
        member = userRepository.save(createActivateUser("member"));
        study = studyRepository.save(createStudy(leader));
    }

    @Test
    @WithUserDetails
    void 스터디_생성_성공() throws Exception {
        login(leader);

        StudyTechStack studyTechStack = new StudyTechStack(Java);
        StudyCreateRequestStudyPeriodDto periodDto = new StudyCreateRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyCreateRequestStudyNeedCareerDto careerDto = new StudyCreateRequestStudyNeedCareerDto("직무", 10);
        StudyCreateRequestStudyFileDto fileDto = new StudyCreateRequestStudyFileDto("imageURL", "originalName");

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study",
                        APPLICATION_JSON,
                        new StudyCreateRequest("팀", "정보", 4, HARD, periodDto, careerDto, List.of(studyTechStack), List.of(fileDto)),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/create",
                        DOCS_TAG,
                        "스터디 생성",
                        null, null
                )
        ).andExpect(status().isOk());

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
        assertThat(foundStudy.getFiles()).isNotNull();
    }

    @Test
    @WithUserDetails
    void 스터디_수정_성공() throws Exception {
        login(leader);

        StudyTechStack studyTechStack = new StudyTechStack(Java);
        StudyUpdateRequestStudyPeriodDto periodDto = new StudyUpdateRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyUpdateRequestStudyNeedCareerDto careerDto = new StudyUpdateRequestStudyNeedCareerDto("updateOccupation", 5);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study/update",
                        APPLICATION_JSON,
                        new StudyUpdateRequest(study.getId(), "updateTeam", "updateInfo", 5, HARD, periodDto, careerDto, List.of(studyTechStack)),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/update",
                        DOCS_TAG,
                        "스터디 수정",
                        null, null
                )
        ).andExpect(status().isOk());

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
        login(leader);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study/proceed",
                        APPLICATION_JSON,
                        new StudyProceedRequest(study.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/proceed",
                        DOCS_TAG,
                        "스터디 진행",
                        null, null
                )
        ).andExpect(status().isOk());

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(IN_PROGRESS);
    }

    @Test
    @WithUserDetails
    void 스터디_멤버_추방_성공() throws Exception {
        login(leader);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study/remove-member",
                        APPLICATION_JSON,
                        new StudyRemoveMemberRequest(study.getId(), member.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/remove-member",
                        DOCS_TAG,
                        "스터디 멤버 추방",
                        null, null
                )
        ).andExpect(status().isOk());

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getMembers()).doesNotContain(member);
    }

    @Test
    @WithUserDetails
    void 스터디_닫기_성공() throws Exception {
        login(leader);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study/close",
                        APPLICATION_JSON,
                        new StudyCloseRequest(study.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/close",
                        DOCS_TAG,
                        "스터디 닫기",
                        null, null
                )
        ).andExpect(status().isOk());

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(CLOSED);
    }

    @Test
    @WithUserDetails
    void 스터디_나가기_성공() throws Exception {
        login(member);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study/leave",
                        APPLICATION_JSON,
                        new StudyLeaveRequest(study.getId(), member.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/leave",
                        DOCS_TAG,
                        "스터디 나가기",
                        null, null
                )
        ).andExpect(status().isOk());

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getMembers()).doesNotContain(member);
    }

    @Test
    @WithUserDetails
    void 스터디_삭제_성공() throws Exception {
        login(leader);

        mockMvc.perform(
                deleteMethodRequestBuilder(
                        "/study",
                        APPLICATION_JSON,
                        new StudyDeleteRequest(study.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/delete",
                        DOCS_TAG,
                        "스터디 나가기",
                        null, null
                )
        ).andExpect(status().isOk());

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(DELETED);
    }

    @Test
    @WithUserDetails
    void 스터디_오픈_성공() throws Exception {
        login(leader);

        study.proceed();

        StudyOpenRequestStudyPeriodDto periodDto = new StudyOpenRequestStudyPeriodDto(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/study/open",
                        APPLICATION_JSON,
                        new StudyOpenRequest(study.getId(), leader.getId(), periodDto),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/open",
                        DOCS_TAG,
                        "스터디 오픈",
                        null, null
                )
        ).andExpect(status().isOk());

        Study foundStudy = studyRepository.findById(study.getId()).orElseThrow();

        assertThat(foundStudy.getStatus()).isEqualTo(OPENED);
    }

    @Test
    @WithUserDetails
    void 유저가_참여한_스터디_조회_성공() throws Exception {
        유저가_참여한_스터디_조회_스터디_생성(leader);

        GetStudyCardsParam req = new GetStudyCardsParam(member.getId(), "open", null);

        List<Pair<String, String>> param = new ArrayList<>();
        param.add(Pair.of("userId", req.userId().toString()));
        param.add(Pair.of("statusCond", req.statusCond()));

        mockMvc.perform(
                getMethodRequestBuilder(
                        "/study/my-study",
                        token,
                        param
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/my-study",
                        DOCS_TAG,
                        "유저가 참여한 스터디 조회",
                        null, null
                )
        ).andExpect(
                status().isOk()
        ).andExpectAll(
                유저가_참여한_스터디_조회_검증(req)
        );
    }

    @Test
    @WithUserDetails
    void 스터디_이미지_업로드_성공() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        mockMvc.perform(
                multipartRequestBuilder(
                        "/study/image-upload",
                        List.of(
                                multipartFile
                        ),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/image-upload",
                        DOCS_TAG,
                        "스터디 이미지 수정",
                        null, null
                )
        ).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails
    void 유저가_즐겨찾기한_스터디_조회_성공() throws Exception{

        GetBookmarkStudyCardsCond.GetBookmarkStudyCardsParam req = new GetBookmarkStudyCardsCond.GetBookmarkStudyCardsParam(member.getId());

        List<Pair<String, String>> param = new ArrayList<>();
        param.add(Pair.of("userId", req.userId().toString()));

        mockMvc.perform(
                getMethodRequestBuilder(
                        "/study/my-bookmark-study",
                        token,
                        param
                )
        ).andDo(
                getRestDocumentationResult(
                        "study/my-bookmark-study",
                        DOCS_TAG,
                        "유저가 즐겨찾기한 스터디 조회",
                        null, null
                )
        ).andExpect(
                status().isOk()
        ).andExpectAll(유저가_즐겨찾기한_스터디_조회_검증()
        );
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        Study study1 = studyRepository.save(createStudy(leader, Spring, Java));
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

    private ResultMatcher[] 유저가_참여한_스터디_조회_검증(GetStudyCardsParam req) {
        List<Study> studiesContainsMember = getStudiesByFilter(req);

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
                            jsonPath("$.studies.[" + index + "].techStackList[*]").value(stringTechStackList(indexStudy))
                    ));
                })
                .flatMap(entry -> entry.getValue().stream())
                .toArray(ResultMatcher[]::new);
    }

    private List<String> stringTechStackList(Study indexStudy) {
        List<String> techStackList = indexStudy.getTechStackList()
                .stream()
                .map(StudyTechStack::getTechStack)
                .map(Enum::name)
                .collect(Collectors.toList());
        return techStackList;
    }

    private List<Study> getStudiesByFilter(GetStudyCardsParam req) {
        List<Study> studiesContainsMember = studyRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() != DELETED && it.getStatus() != CLOSED && it.containsMember(member))
                .filter(study -> {
                    if (req.techStackList() != null) {
                        study.getTechStackList()
                                .stream()
                                .anyMatch(techStack -> req.techStackList().stream()
                                        .anyMatch(stack -> stack.equals(techStack.getTechStack().toString())));
                    }
                    return false;
                })
                .collect(Collectors.toList());
        return studiesContainsMember;
    }

    private ResultMatcher[] 유저가_즐겨찾기한_스터디_조회_검증(){
        List<Study> bookmarkStudies = getBookmarkStudiesByFilter();

        return  IntStream.range(0, bookmarkStudies.size())
                .mapToObj(index -> {
                    Study indexStudy = bookmarkStudies.get(index);

                    return Map.entry(index, List.of(
                            jsonPath("$.studies.[" + index + "].studyName").value(indexStudy.getName()),
                            jsonPath("$.studies.[" + index + "].studyPeriod").isNotEmpty(),
                            jsonPath("$.studies.[" + index + "].leaderName").value(indexStudy.getLeader().getName()),
                            jsonPath("$.studies.[" + index + "].commentCount").value(indexStudy.getComments().size()),
                            jsonPath("$.studies.[" + index + "].viewCount").value(indexStudy.getViews().size()),
                            jsonPath("$.studies.[" + index + "].bookmarkCount").value(indexStudy.getBookmarks().size()),
                            jsonPath("$.studies.[" + index + "].maxMemberSize").value(indexStudy.getMaxMembersSize())
                    ));
                })
                .flatMap(entry -> entry.getValue().stream())
                .toArray(ResultMatcher[]::new);
    }

    private List<Long> 유저가_북마크한_스터디_아이디_리스트(User member){

        return  bookmarkRepository.findByUser(member)
                .stream()
                .map(bookmark -> bookmark.getStudy().getId()).
                collect(Collectors.toList());
    }

    private List<Study> getBookmarkStudiesByFilter() {
        
        List<Study> studiesContainsMember = studyRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() != DELETED && it.getStatus() != CLOSED)
                .filter(study -> 유저가_북마크한_스터디_아이디_리스트(member).contains(study.getId()))
                .collect(Collectors.toList());
        
        return studiesContainsMember;
    }
}
