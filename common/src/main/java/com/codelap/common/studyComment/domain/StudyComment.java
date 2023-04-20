package com.codelap.common.studyComment.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

import static com.codelap.common.studyComment.domain.StudyCommentStatus.*;
import static com.codelap.common.studyComment.domain.StudyCommentStatus.CREATED;
import static com.codelap.common.support.Preconditions.check;
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

    @Setter
    @Enumerated(STRING)
    private StudyCommentStatus status = CREATED;

    public boolean isUser(User user) {
        return this.user == user;
    }

    private StudyComment(Study study, User user, String comment) {
        this.study = study;
        this.user = user;
        this.comment = comment;
    }

    public static StudyComment create(Study study, User user, String comment) {
        require(nonNull(study));
        require(nonNull(user));
        require(isNotBlank(comment));

        StudyComment studyComment = new StudyComment(study, user, comment);
        study.addComment(studyComment);

        return studyComment;
    }

    public void update(String comment){
        require(isNotBlank(comment));

        this.comment = comment;
    }

    public void delete(){
        check(CAN_DELETE_STATUS.contains(status));

        this.status = DELETED;
    }
}
