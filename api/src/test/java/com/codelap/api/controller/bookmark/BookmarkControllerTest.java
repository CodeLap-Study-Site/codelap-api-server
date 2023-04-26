package com.codelap.api.controller.bookmark;

import com.codelap.api.controller.Bookmark.dto.BookmarkDeleteDto;
import com.codelap.api.support.ApiTest;
import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.bookmark.service.BookmarkService;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static com.codelap.api.controller.Bookmark.dto.BookmarkCreateDto.BookmarkCreateRequest;
import static com.codelap.api.controller.Bookmark.dto.BookmarkDeleteDto.*;
import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookmarkControllerTest extends ApiTest {

    @Autowired
    private StudyRepository studyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BookmarkService bookmarkService;

    private Bookmark bookmark;

    private User leader;

    private User member;

    private Study study;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setUp"));

        member = userRepository.save(User.create("member", 10, career, "abcd", "member"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, Arrays.asList(Java, Spring)));
    }

    @Test
    void 북마크_생성_성공() throws Exception {
        BookmarkCreateRequest req = new BookmarkCreateRequest(study.getId(), member.getId());

        mockMvc.perform(post("/bookmark")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("bookmark/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        Bookmark foundBookmark = bookmarkRepository.findAll().get(0);

        assertThat(foundBookmark.getCreatedAt()).isNotNull();
    }

    @Test
    void 북마크_삭제_성공() throws  Exception {
        Bookmark bookmark = bookmarkRepository.save(Bookmark.create(study, member));

        BookmarkDeleteRequest req = new BookmarkDeleteRequest(bookmark.getId(), member.getId());

        mockMvc.perform(delete("/bookmark")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("bookmark/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(bookmarkRepository.findById(bookmark.getId())).isEmpty();
    }
}
