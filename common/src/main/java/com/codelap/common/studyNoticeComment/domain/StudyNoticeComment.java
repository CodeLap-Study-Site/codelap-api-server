package com.codelap.common.studyNoticeComment.domain;

import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.CREATED;
import static com.codelap.common.support.Preconditions.require;
import static jakarta.persistence.EnumType.STRING;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyNoticeComment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private StudyNotice studyNotice;

    @ManyToOne
    private User user;

    private String content;

    private final OffsetDateTime createAt = OffsetDateTime.now();

    @Enumerated(STRING)
    private final StudyNoticeCommentStatus status = CREATED;

    private StudyNoticeComment(User user, String content) {
        this.user = user;
        this.content = content;
    }

    public static StudyNoticeComment create(StudyNotice studyNotice, User user, String content) {
        require(nonNull(studyNotice));
        require(nonNull(user));
        require(isNotBlank(content));

        return new StudyNoticeComment(user, content);
    }
}
