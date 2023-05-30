package com.codelap.api.service.study;

import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.dto.GetStudiesCardDto;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyView.service.StudyViewService;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static com.codelap.common.support.TechStack.*;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class DefaultStudyAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;

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

    @Test
    void 유저가_참여한_스터디_조회_성공() {
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());

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

    @Test
    void 유저가_즐겨찾기한_스터디_조회_성공() {
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());

        유저가_참여한_스터디_조회_스터디_생성(leader);

        List<GetStudiesCardDto.GetStudyInfo> allStudies = studyAppService.getBookmarkedStudiesByUser(member.getId());

        List<Study> studies = studyRepository.findAll()
                .stream()
                .filter(study -> study.getStatus() == OPENED)
                .filter(study -> study.getTechStackList()
                        .stream()
                        .anyMatch(techStack -> techStack.getTechStack().equals(React)))
                        .collect(Collectors.toList());

        System.out.println(allStudies);
        Assertions.assertThat(studies.size()).isEqualTo(allStudies.size());
    }

    @Test
    void 스터디_이미지_업데이트() throws Exception {
        leader = userRepository.save(createActivateUser("member"));
        Study study = studyRepository.save(createStudy(leader));

        MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        studyAppService.imageUpload(leader.getId(), study.getId(), file);

        assertThat(study.getFiles()).isNotNull();
    }

    private void 유저가_참여한_스터디_조회_스터디_생성(User leader) {
        Study study1 = studyRepository.save(createStudy(leader, Spring, Java));

        study1.addMember(member);

        studyCommentService.create(study1.getId(), member.getId(), "message");
        studyViewService.create(study1.getId(), "1.1.1.1");
        bookmarkService.create(study1.getId(), member.getId());

        Study study2 = studyRepository.save(createStudy(leader, JavaScript, React));

        study2.addMember(member);

        studyCommentService.create(study2.getId(), member.getId(), "message");
        studyViewService.create(study2.getId(), "1.1.1.2");
    }
}