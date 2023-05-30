package com.codelap.api.service.bookmark;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.bookmark.dto.GetBookmarkCardDto;
import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.study.domain.StudyStatus;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyView.service.StudyViewService;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.fixture.StudyFixture;
import com.codelap.fixture.UserFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.codelap.common.support.TechStack.*;
import static com.codelap.fixture.StudyFixture.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class DefaultBookmarkAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookmarkAppService bookmarkAppService;

    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    StudyCommentService studyCommentService;

    @Autowired
    StudyViewService studyViewService;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    BookmarkRepository bookmarkRepository;
    private User leader;

    private User member;

    @Test
    void 유저가_즐겨찾기한_스터디_조회_성공() {
        leader = userRepository.save(UserFixture.createActivateUser());
        member = userRepository.save(UserFixture.createActivateUser());

        유저가_즐겨찾기한_스터디_조회_스터디_생성(leader);

        List<Bookmark> bookmarks = bookmarkRepository.findAll();


        List<GetBookmarkCardDto.GetBookmarkInfo> allStudies = bookmarkAppService.getBookmarkStudiesByUser(member.getId(), "open", bookmarks, List.of(Java));

        List<Study> studies = studyRepository.findAll()
                .stream()
                .filter(study -> study.getStatus() == StudyStatus.OPENED)
                .filter(study -> study.containsBookmark(bookmarks.get(0)))
                .filter(study -> study.getTechStackList()
                        .stream()
                        .anyMatch(techStack -> techStack.getTechStack().equals(React)))
                .collect(Collectors.toList());

        assertThat(studies.size()).isEqualTo(allStudies);
    }

    private void 유저가_즐겨찾기한_스터디_조회_스터디_생성(User leader){
       Study study1 =  studyRepository.save(createStudy(leader, Spring , Java));

        studyCommentService.create(study1.getId(), member.getId(), "message");
        studyViewService.create(study1.getId(), "1.1.1.1");
        bookmarkService.create(study1.getId(), member.getId());

        Study study2 = studyRepository.save(createStudy(leader, JavaScript, React));

        studyCommentService.create(study2.getId(), member.getId(), "message");
        studyViewService.create(study2.getId(), "1.1.1.2");
        bookmarkService.create(study2.getId(), member.getId());
    }
}
