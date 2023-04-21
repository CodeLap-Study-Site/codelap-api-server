package com.codelap.api.service.study;

import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.*;
import com.codelap.common.study.dto.GetMyStudiesDto;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.TechStack.*;
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
    StudyQueryAppService studyQueryAppService;

    @Autowired
    BookmarkService bookmarkService;

    private User leader;

    private User member;
    private List<TechStack> techStackList;
    private Study study1;
    private Study study2;

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));
        member = userRepository.save(User.create("member", 10, career, "abcd", "email"));

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<GetMyStudiesDto> allStudies = studyQueryAppService.getAttendedStudiesByUser(member);

        Map<Long, List<GetTechStackDto>> techStacksMap = studyQueryAppService.getTechStacks(스터디_아이디_리스트_가져오기(allStudies))
                .stream()
                .collect(Collectors.groupingBy(GetTechStackDto::getStudyId));

        allStudies.forEach(it -> it.setTechStackList(techStacksMap.get(it.getStudyId())));

        IntStream.range(0, allStudies.size())
                .forEach(index -> {
                    GetMyStudiesDto study = allStudies.get(index);

                    List<TechStack> techStacksByStudy = study.getTechStackList()
                            .stream()
                            .map(getTechStackDto -> getTechStackDto.getTechStackList())
                            .collect(Collectors.toList());

                    if (index == 0) {
                        assertThat(study.getCommentCount()).isEqualTo(study1.getComments().size());
                        assertThat(study.getViewCount()).isEqualTo(study1.getViews().size());
                        assertThat(study.getBookmarkCount()).isEqualTo(study1.getBookmarks().size());
                        assertThat(techStacksByStudy).isEqualTo(study1.getTechStackList());
                    } else if (index == 1) {
                        assertThat(study.getCommentCount()).isEqualTo(study2.getComments().size());
                        assertThat(study.getViewCount()).isEqualTo(study2.getViews().size());
                        assertThat(study.getBookmarkCount()).isEqualTo(study2.getBookmarks().size());
                        assertThat(techStacksByStudy).isEqualTo(study2.getTechStackList());
                    }
                });
    }

    private List<Long> 스터디_아이디_리스트_가져오기(List<GetMyStudiesDto> allStudies) {
        return allStudies.stream().map(study -> study.getStudyId()).collect(Collectors.toList());
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
}