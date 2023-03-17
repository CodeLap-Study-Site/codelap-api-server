package com.codelap.common.studyConfirmation.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;
import java.util.List;

import static com.codelap.common.studyConfirmation.domain.StudyConfirmationStatus.CREATED;
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

    private final StudyConfirmationStatus status = CREATED;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    private OffsetDateTime confirmedAt;

    private String rejectedMessage;

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
}
