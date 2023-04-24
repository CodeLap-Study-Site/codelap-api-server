package com.codelap.common.bookmark.service;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.bookmark.domain.BookmarkRepository;
import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyNeedCareer;
import com.codelap.common.study.domain.StudyPeriod;
import com.codelap.common.study.domain.StudyRepository;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static com.codelap.common.study.domain.StudyDifficulty.NORMAL;
import static com.codelap.common.study.domain.TechStack.Java;
import static com.codelap.common.study.domain.TechStack.Spring;
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
        UserCareer career = UserCareer.create("직무", 1);
        leader = userRepository.save(User.create("name", 10, career, "abcd", "setUp"));

        member = userRepository.save(User.create("member", 10, career, "abcd", "member"));

        StudyPeriod period = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));
        StudyNeedCareer needCareer = StudyNeedCareer.create("직무", 1);

        study = studyRepository.save(Study.create("팀", "설명", 4, NORMAL, period, needCareer, leader, Arrays.asList(Java, Spring)));
    }

    @Test
    void 북마크_생성_성공() {
        bookmarkService.create(study.getId(), member.getId());

        Bookmark bookmark = bookmarkRepository.findAll().get(0);

        assertThat(bookmark.getId()).isNotNull();
    }
}
