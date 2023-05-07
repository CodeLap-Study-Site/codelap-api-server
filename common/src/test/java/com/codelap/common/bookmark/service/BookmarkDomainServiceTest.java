package com.codelap.common.bookmark.service;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.codelap.common.support.CodeLapExceptionTest.assertThatActorValidateCodeLapException;
import static com.codelap.fixture.StudyFixture.createStudy;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest
public class BookmarkDomainServiceTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BookmarkService bookmarkService;

    private User leader;

    private User member;

    private Study study;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());

        study = studyRepository.save(createStudy(leader));
    }

    @Test
    void 북마크_생성_성공() {
        bookmarkService.create(study.getId(), member.getId());

        Bookmark bookmark = bookmarkRepository.findAll().get(0);

        assertThat(bookmark.getId()).isNotNull();
    }

    @Test
    void 북마크_삭제_성공() {
        bookmarkService.create(study.getId(), member.getId());

        Bookmark bookmark = bookmarkRepository.findAll().get(0);

        bookmarkService.delete(bookmark.getId(), member.getId());

        assertThat(bookmarkRepository.findById(bookmark.getId()).isEmpty());
        assertThat(study.containsBookmark(bookmark)).isFalse();
    }

    @Test
    void 북마크_삭제_실패__북마크를_한_사용자가_아님() {
        bookmarkService.create(study.getId(), member.getId());

        Bookmark bookmark = bookmarkRepository.findAll().get(0);

        User fakeMember = userRepository.save(createActivateUser("fakeUser"));

        assertThatActorValidateCodeLapException().isThrownBy(() -> bookmarkService.delete(bookmark.getId(), fakeMember.getId()));
    }
}
