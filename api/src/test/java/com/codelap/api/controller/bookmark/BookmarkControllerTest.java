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
import static com.codelap.api.support.RestDocumentationUtils.*;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkControllerTest extends ApiTest {

    private static final String DOCS_TAG = "Bookmark";

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

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/bookmark",
                        APPLICATION_JSON,
                        new BookmarkCreateRequest(study.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "bookmark/create",
                        DOCS_TAG,
                        "북마크 생성",
                        null, null
                )
        ).andExpect(status().isOk());

        Bookmark foundBookmark = bookmarkRepository.findAll().get(0);

        assertThat(foundBookmark.getCreatedAt()).isNotNull();
    }

    @Test
    @WithUserDetails
    void 북마크_삭제_성공() throws Exception {
        login(member);

        Bookmark bookmark = bookmarkRepository.save(Bookmark.create(study, member));

        mockMvc.perform(
                deleteMethodRequestBuilder(
                        "/bookmark",
                        APPLICATION_JSON,
                        new BookmarkDeleteRequest(bookmark.getId()),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "bookmark/delete",
                        DOCS_TAG,
                        "북마크 삭제",
                        null, null
                )
        ).andExpect(status().isOk());

        assertThat(bookmarkRepository.findById(bookmark.getId())).isEmpty();
        assertThat(study.containsBookmark(bookmark)).isFalse();
    }
}
