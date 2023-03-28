package com.codelap.common.bookmark.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.study.domain.StudyStatus;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private User user;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    public static Bookmark create(Study study, User user) {
        require(!study.isLeader(user));

        check(study.getStatus() != StudyStatus.DELETED);
        check(user.getStatus() != UserStatus.DELETED);

        return new Bookmark();
    }
}