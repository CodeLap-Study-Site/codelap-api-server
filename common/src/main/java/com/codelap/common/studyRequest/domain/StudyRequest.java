package com.codelap.common.studyRequest.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;

import static com.codelap.common.studyRequest.domain.StudyRequestStatus.*;
import static com.codelap.common.support.Preconditions.check;
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
    private String message;

    private String rejectMessage;

    @Setter
    @Enumerated(STRING)
    private StudyRequestStatus status = REQUESTED;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    public boolean isLeader(User leader) {
        return this.study.getLeader() == leader;
    }

    private StudyRequest(User user, Study study, String message) {
        this.user = user;
        this.study = study;
        this.message = message;
    }

    public static StudyRequest create(User user, Study study, String message) {
        require(nonNull(user));
        require(nonNull(study));
        require(Strings.isNotBlank(message));
        require(!study.containsMember(user));

        return new StudyRequest(user, study, message);
    }

    public void approve() {
        check(status == REQUESTED);

        this.status = APPROVED;
    }

    public void reject(String rejectMessage) {
        require(Strings.isNotBlank(rejectMessage));

        check(status == REQUESTED);

        this.status = REJECTED;
        this.rejectMessage = rejectMessage;
    }
}
