package com.codelap.api.service.study;

import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.*;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyView.service.StudyViewService;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static com.codelap.common.study.domain.TechStack.*;

@Transactional
@SpringBootTest
class DefaultStudyAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyAppService studyAppService;

    @Autowired
    StudyCommentService studyCommentService;

    @Autowired
    StudyViewService studyViewService;

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

        List<GetStudiesCardDto.GetStudyInfo> allStudies = studyAppService.getAttendedStudiesByUser(member.getId(), "open", List.of((Java)));
        List<Study> studies = studyRepository.findAll()
                .stream()
                .filter(study -> study.getStatus() == OPENED)
                .filter(study -> study.containsMember(member))
                .filter(study -> study.getTechStackList()
                        .stream()
                        .anyMatch(techStack -> techStack.getTechStack().equals(React)))
                .collect(Collectors.toList());

        Assertions.assertThat(studies.size()).isEqualTo(allStudies.size());
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        techStackList = Arrays.asList(new StudyTechStack(Spring), new StudyTechStack(Java));

        study1 = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, Arrays.asList(new StudyTechStack(Spring), new StudyTechStack(Java))));

        study1.addMember(member);

        studyCommentService.create(study1.getId(), member.getId(), "message");
        studyViewService.create(study1.getId(), "1.1.1.1");
        bookmarkService.create(study1.getId(), member.getId());

        study2 = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, Arrays.asList(new StudyTechStack(JavaScript), new StudyTechStack(React))));

        study2.addMember(member);

        studyCommentService.create(study2.getId(), member.getId(), "message");
        studyViewService.create(study2.getId(), "1.1.1.2");
    }
}