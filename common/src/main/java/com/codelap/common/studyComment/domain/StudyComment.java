package com.codelap.common.studyComment.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static com.codelap.common.studyComment.domain.StudyCommentStatus.CREATED;
import static com.codelap.common.support.Preconditions.require;
import static jakarta.persistence.EnumType.STRING;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private User user;

    private String comment;

    private final OffsetDateTime createAt = OffsetDateTime.now();

    @Enumerated(STRING)
    private final StudyCommentStatus status = CREATED;

    private StudyComment(User user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    public static StudyComment create(Study study, User user, String comment) {
        require(nonNull(study));
        require(nonNull(user));
        require(isNotBlank(comment));

        return new StudyComment(user, comment);
    }
}
