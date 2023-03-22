package com.codelap.common.studyNotice.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.CREATED_NOTICE;
import static com.codelap.common.support.Preconditions.require;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class StudyNotice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Study study;

    @ManyToOne
    private User leader;

    @OneToMany
    private final List<User> readNoticeUsers = new ArrayList<>();

    private String title;

    private String message;

    @ElementCollection
    private List<StudyNoticeFile> files;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    private final StudyNoticeStatus status = CREATED_NOTICE;

    public static boolean isContents(String message, List<StudyNoticeFile> files) {
        if (isBlank(message) && isNull(files)) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    private StudyNotice(String title, String message, List<StudyNoticeFile> files) {
        this.title = title;
        this.message = message;
        this.files = files;
    }

    public static StudyNotice create(String title, String message, List<StudyNoticeFile> files) {
        require(isNotBlank(title));

        isContents(message, files);

        return new StudyNotice(title, message, files);
    }
}
