package com.codelap.common.studyNotice.domain;

import com.codelap.common.study.domain.Study;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.*;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.CREATED;
import static com.codelap.common.studyNotice.domain.StudyNoticeStatus.DELETED;
import static com.codelap.common.support.Preconditions.check;
import static com.codelap.common.support.Preconditions.require;
import static jakarta.persistence.EnumType.STRING;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PROTECTED;
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

    @ManyToMany
    private final List<User> readMembers = new ArrayList<>();

    private String title;

    private String contents;

    @ElementCollection
    private List<StudyNoticeFile> files;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    @Setter
    @Enumerated(STRING)
    private StudyNoticeStatus status = CREATED;

    public boolean isLeader(User leader) {
        return this.study.getLeader() == leader;
    }

    private StudyNotice(Study study, String title, String contents, List<StudyNoticeFile> files) {
        this.study = study;
        this.title = title;
        this.contents = contents;
        this.files = files;
    }

    public static StudyNotice create(Study study, String title, String contents, List<StudyNoticeFile> files) {
        require(nonNull(study));
        require(isNotBlank(title));
        require(isNotBlank(contents));

        return new StudyNotice(study, title, contents, files);
    }

    public void update(String title, String contents, List<StudyNoticeFile> files){
        require(isNotBlank(title));
        require(isNotBlank(contents));

        this.title = title;
        this.contents = contents;
        this.files = files;
    }

    public void delete(){
        check(CAN_DELETE_STATUS.contains(status));

        this.status = DELETED;
    }
}
