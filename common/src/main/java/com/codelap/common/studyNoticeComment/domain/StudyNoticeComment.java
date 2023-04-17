package com.codelap.common.studyNoticeComment.domain;

import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

import static com.codelap.common.studyNoticeComment.domain.StudyNoticeCommentStatus.*;
import static com.codelap.common.support.Preconditions.check;
import static com.codelap.common.support.Preconditions.require;
import static jakarta.persistence.EnumType.STRING;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyNoticeComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private StudyNotice studyNotice;

    @ManyToOne
    private User user;

    private String content;

    private final OffsetDateTime createAt = OffsetDateTime.now();

    @Setter
    @Enumerated(STRING)
    private StudyNoticeCommentStatus status = CREATED;
    public boolean isUser(User user) {
        return this.user == user;
    }
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

    public void delete() {
        check(CAN_DELETE_STATUS.contains(status));

        this.status = DELETED;
    }

    public void update(String content){
        require(isNotBlank(content));

        this.content = content;
    }
}
