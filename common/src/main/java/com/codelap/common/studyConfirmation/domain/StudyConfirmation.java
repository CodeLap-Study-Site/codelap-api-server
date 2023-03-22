package com.codelap.common.studyConfirmation.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;
import java.util.List;

import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.*;
import static com.codelap.common.support.Preconditions.check;
import static com.codelap.common.support.Preconditions.require;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class StudyConfirmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private User user;

    private String title;

    private String content;

    @ElementCollection
    private List<StudyConfirmationFile> files;

    @Setter
    private StudyConfirmationStatus status = CREATED;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    private OffsetDateTime confirmedAt;
    private OffsetDateTime rejectedAt;
    private String rejectedMessage;

    public boolean isUser(User user) {
        return this.user == user;
    }

    public boolean isLeader(User leader) {
        return this.study.getLeader() == leader;
    }

    private StudyConfirmation(Study study, User user, String title, String content, List<StudyConfirmationFile> files) {
        this.study = study;
        this.user = user;
        this.title = title;
        this.content = content;
        this.files = files;
    }

    public static StudyConfirmation create(Study study, User user, String title, String content, List<StudyConfirmationFile> files) {
        require(nonNull(study));
        require(nonNull(user));
        require(Strings.isNotBlank(title));
        require(Strings.isNotBlank(content));
        require(nonNull(files));

        return new StudyConfirmation(study, user, title, content, files);
    }

    public void confirm() {
        check(this.status == CREATED);

        this.confirmedAt = OffsetDateTime.now();
        this.status = CONFIRMED;
    }

    public void reject(String rejectedMessage) {
        check(this.status == CREATED);

        require(isNotBlank(rejectedMessage));
        this.rejectedAt = OffsetDateTime.now();
        this.rejectedMessage = rejectedMessage;
        this.status = REJECTED;
    }
}
