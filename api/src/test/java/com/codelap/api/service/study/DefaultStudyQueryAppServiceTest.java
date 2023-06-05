package com.codelap.api.service.study;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyView.service.StudyViewService;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Member;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static com.codelap.common.study.dto.GetStudiesCardDto.GetStudyInfo;
import static com.codelap.common.study.dto.GetStudiesCardDto.GetTechStackInfo;
import static com.codelap.common.support.TechStack.*;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
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

    @Autowired
    BookmarkRepository bookmarkRepository;

    private User leader;
    private User member;

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<GetStudyInfo> allStudies = studyQueryDslAppService.findStudyCardsByCond(member, "open", List.of(React));

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

    @Test
    void 유저가_즐겨찾기한_스터디_조회_성공(){
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<GetStudyInfo> allStudies = studyQueryDslAppService.getBookmarkedStudiesByUser(유저가_북마크한_스터디_아이디_리스트(member));

        List<Study> studyList = studyRepository.findAll()
                .stream()
                .filter(study -> study.getStatus() == OPENED)
                .filter(study -> 유저가_북마크한_스터디_아이디_리스트(member).contains(study.getId()))
                .collect(Collectors.toList());

        IntStream.range(0, allStudies.size())
                .forEach(index -> {
                    GetStudyInfo study = allStudies.get(index);

                    assertThat(study.getStudyId()).isEqualTo(studyList.get(index).getId());
                    assertThat(study.getBookmarkCount()).isEqualTo(studyList.get(index).getBookmarks().size());
                    assertThat(study.getCommentCount()).isEqualTo(studyList.get(index).getComments().size());
                    assertThat(study.getViewCount()).isEqualTo(studyList.get(index).getViews().size());
                });
    }

    private List<Long> 유저가_북마크한_스터디_아이디_리스트(User member){
        return  bookmarkRepository.findByUser(member)
                .stream()
                .map(bookmark -> bookmark.getStudy().getId()).
                collect(Collectors.toList());
    }

    private List<Long> 스터디_아이디_리스트_가져오기(List<GetStudyInfo> allStudies) {
        return allStudies.stream().map(study -> study.getStudyId()).collect(Collectors.toList());
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        Study study1 = studyRepository.save(createStudy(leader, Spring, Java));

        study1.addMember(member);

        studyCommentService.create(study1.getId(), member.getId(), "message");
        studyViewService.create(study1.getId(), "1.1.1.1");
        bookmarkService.create(study1.getId(), member.getId());

        Study study2 = studyRepository.save(createStudy(leader, JavaScript, React));

        study2.addMember(member);
    }
}