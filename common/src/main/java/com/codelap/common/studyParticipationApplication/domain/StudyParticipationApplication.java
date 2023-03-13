package com.codelap.common.studyParticipationApplication.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import static com.codelap.common.study.domain.StudyStatus.*;
import static com.codelap.common.studyParticipationApplication.domain.ApplicationStatus.*;
import static com.codelap.common.support.Preconditions.*;
import static com.codelap.common.user.domain.UserStatus.DELETED;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class StudyParticipationApplication {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Study study;

    @Setter
    @Enumerated(STRING)
    private ApplicationStatus applicationStatus = REQUESTED;

    private String message;

    private String refuseMessage;

    private StudyParticipationApplication(User user, Study study, String message) {
        this.user = user;
        this.study = study;
        this.message = message;
    }

    public static StudyParticipationApplication create(User user, Study study, String message, ApplicationStatus applicationStatus) {
        require(nonNull(user));
        require(nonNull(study));
        require(Strings.isNotBlank(message));

        check(study.getStatus().equals(OPENED));
        check(!user.getStatus().equals(DELETED));
        check(applicationStatus == REQUESTED);

        return new StudyParticipationApplication(user, study, message);
    }
}
