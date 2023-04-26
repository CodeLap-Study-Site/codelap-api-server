package com.codelap.common.bookmark.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyStatus;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static com.codelap.common.support.Preconditions.check;
import static com.codelap.common.support.Preconditions.require;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private User user;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    public boolean isUser(User user) {
        return this.user == user;
    }

    private Bookmark(Study study, User user) {
        this.study = study;
        this.user = user;
    }

    public static Bookmark create(Study study, User user) {
        require(!study.isLeader(user));

        check(study.getStatus() != StudyStatus.DELETED);
        check(user.getStatus() != UserStatus.DELETED);

        Bookmark bookmark = new Bookmark(study, user);
        study.addBookmark(bookmark);

        return bookmark;
    }

    public void delete(Study study, User user){
        require(!study.isLeader(user));

        check(study.getStatus() != StudyStatus.DELETED);
        check(user.getStatus() != UserStatus.DELETED);

        study.removeBookmark(this);
    }
}