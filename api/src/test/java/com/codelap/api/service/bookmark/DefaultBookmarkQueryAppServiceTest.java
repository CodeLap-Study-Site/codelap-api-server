package com.codelap.api.service.bookmark;

import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyView.service.StudyViewService;
import com.codelap.common.support.TechStack;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.fixture.StudyFixture;
import com.codelap.fixture.UserFixture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class DefaultBookmarkQueryAppServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    BookmarkQueryAppService bookmarkQueryAppService;

    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    StudyCommentService studyCommentService;

    @Autowired
    StudyViewService studyViewService;

    private User user;

    private User member;


    private void 유저가_즐겨찾기한_스터디_조회_스터디_생성(User leader){

        Study study1 = studyRepository.save(StudyFixture.createStudy(leader, TechStack.Spring, TechStack.Java));

        studyCommentService.create(study1.getId(), member.getId(), "message");
        studyViewService.create(study1.getId(), "1.1.1.1");
        bookmarkService.create(study1.getId(), member.getId());
    }
}
