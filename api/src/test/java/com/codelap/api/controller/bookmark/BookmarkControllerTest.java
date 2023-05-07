package com.codelap.api.controller.bookmark;

import com.codelap.api.support.ApiTest;
import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.fixture.StudyFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.codelap.api.controller.Bookmark.dto.BookmarkCreateDto.BookmarkCreateRequest;
import static com.codelap.api.controller.Bookmark.dto.BookmarkDeleteDto.BookmarkDeleteRequest;
import static com.codelap.api.support.HttpMethod.DELETE;
import static com.codelap.api.support.HttpMethod.POST;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;

public class BookmarkControllerTest extends ApiTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    private User leader;

    private User member;

    private Study study;

    @BeforeEach
    void setUp() {
        leader = userRepository.save(createActivateUser());
        member = userRepository.save(createActivateUser());
        study = studyRepository.save(StudyFixture.createStudy(leader));
    }

    @Test
    @WithUserDetails
    void 북마크_생성_성공() throws Exception {
        login(member);

        BookmarkCreateRequest req = new BookmarkCreateRequest(study.getId());

        setMockMvcPerform(POST, req, "/bookmark", "bookmark/create");

        Bookmark foundBookmark = bookmarkRepository.findAll().get(0);

        assertThat(foundBookmark.getCreatedAt()).isNotNull();
    }

    @Test
    @WithUserDetails
    void 북마크_삭제_성공() throws Exception {
        login(member);

        Bookmark bookmark = bookmarkRepository.save(Bookmark.create(study, member));

        BookmarkDeleteRequest req = new BookmarkDeleteRequest(bookmark.getId());

        setMockMvcPerform(DELETE, req, "/bookmark", "bookmark/delete");

        assertThat(bookmarkRepository.findById(bookmark.getId())).isEmpty();
        assertThat(study.containsBookmark(bookmark)).isFalse();
    }
}
