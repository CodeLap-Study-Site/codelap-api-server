package com.codelap.common.studyRequest.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import static com.codelap.common.studyRequest.domain.ApplicationStatus.REQUESTED;
import static com.codelap.common.support.Preconditions.require;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class StudyRequest {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Study study;

    @Setter
    @Enumerated(STRING)
    private ApplicationStatus status = REQUESTED;

    private String message;

    private String rejectMessage;

    private StudyRequest(User user, Study study, String message) {
        this.user = user;
        this.study = study;
        this.message = message;
    }

    public static StudyRequest create(User user, Study study, String message) {
        require(nonNull(user));
        require(nonNull(study));
        require(Strings.isNotBlank(message));

        return new StudyRequest(user, study, message);
    }
}
