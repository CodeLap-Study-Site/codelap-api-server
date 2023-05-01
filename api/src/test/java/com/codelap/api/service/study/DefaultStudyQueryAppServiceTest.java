package com.codelap.api.service.study;

import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.*;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyView.service.StudyViewService;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static com.codelap.common.study.domain.TechStack.*;
import static com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import static com.codelap.common.study.dto.GetStudiesCardDto.GetTechStackInfo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DefaultStudyQueryAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyCommentService studyCommentService;

    @Autowired
    StudyViewService studyViewService;

    @Autowired
    StudyQueryAppService studyQueryDslAppService;

    @Autowired
    BookmarkService bookmarkService;

    private User leader;

    private User member;
    private List<StudyTechStack> techStackList;
    private Study study1;
    private Study study2;
    @Test
    void 유저가_참여한_스터디_조회_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));
        member = userRepository.save(User.create("member", 10, career, "abcd", "email"));

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<TechStack> studyTechStack = new ArrayList<>();
        studyTechStack.add(React);

        List<GetStudyInfo> allStudies = studyQueryDslAppService.getAttendedStudiesByUser(member, "open", studyTechStack);

        Map<Long, List<GetTechStackInfo>> techStacksMap = studyQueryDslAppService.getTechStacks(스터디_아이디_리스트_가져오기(allStudies))
                .stream()
                .collect(Collectors.groupingBy(GetTechStackInfo::getStudyId));

        allStudies.forEach(it -> it.setTechStackList(techStacksMap.get(it.getStudyId())));

        List<Study> studyList = studyRepository.findAll()
                .stream()
                .filter(study -> study.getStatus() == OPENED)
                .filter(study -> study.containsMember(member))
                .filter(study -> study.getTechStackList()
                        .stream()
                        .anyMatch(techStack -> techStack.getTechStack().equals(React)))
                .collect(Collectors.toList());

        IntStream.range(0, allStudies.size())
                .forEach(index -> {
                    GetStudyInfo study = allStudies.get(index);

                    assertThat(study.getStudyId()).isEqualTo(studyList.get(index).getId());
                    assertThat(study.getBookmarkCount()).isEqualTo(studyList.get(index).getBookmarks().size());
                    assertThat(study.getCommentCount()).isEqualTo(studyList.get(index).getComments().size());
                    assertThat(study.getViewCount()).isEqualTo(studyList.get(index).getViews().size());

                    IntStream.range(0, study.getTechStackList().size()).forEach(j -> {
                        assertThat(study.getTechStackList().get(j).getTechStack()).isEqualTo(studyList.get(index).getTechStackList().get(j).getTechStack());
                    });
                });
    }

    private List<Long> 스터디_아이디_리스트_가져오기(List<GetStudyInfo> allStudies) {
        return allStudies.stream().map(study -> study.getStudyId()).collect(Collectors.toList());
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        techStackList = Arrays.asList(new StudyTechStack(Spring), new StudyTechStack(Java));

        study1 = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, Arrays.asList(new StudyTechStack(React), new StudyTechStack(Java))));

        study1.addMember(member);

        studyCommentService.create(study1.getId(), member.getId(), "message");
        studyViewService.create(study1.getId(), "1.1.1.1");
        bookmarkService.create(study1.getId(), member.getId());

        study2 = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, Arrays.asList(new StudyTechStack(JavaScript), new StudyTechStack(React))));

        study2.addMember(member);
    }
}