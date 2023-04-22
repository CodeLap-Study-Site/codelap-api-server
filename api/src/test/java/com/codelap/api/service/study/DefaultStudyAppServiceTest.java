package com.codelap.api.service.study;

import com.codelap.api.service.study.dto.GetStudiesDto.GetStudiesStudyDto;
import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.*;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.StudyStatus.DELETED;
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
    private List<TechStack> techStackList;
    private Study study1;
    private Study study2;

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setup"));

        member = userRepository.save(User.create("member", 10, career, "abcd", "email"));

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<GetStudiesStudyDto> allStudies = studyAppService.getAttendedStudiesByUser(member.getId());
        List<Study> studies = studyRepository.findAll()
                .stream()
                .filter(it -> it.getStatus() != DELETED)
                .filter(it -> it.getMembers().contains(member))
                .collect(Collectors.toList());

        Assertions.assertThat(studies.size()).isEqualTo(allStudies.size());
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