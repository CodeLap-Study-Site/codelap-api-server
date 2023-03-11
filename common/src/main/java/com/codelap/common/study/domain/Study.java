package com.codelap.common.study.domain;

import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.codelap.common.study.domain.StudyStatus.OPENED;
import static com.codelap.common.support.Preconditions.require;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Study {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private String info;

    private int maxMembersSize;

    @Enumerated(STRING)
    private StudyDifficulty difficulty;

    @Embedded
    private StudyPeriod period;

    @Embedded
    private StudyNeedCareer needCareer;

    @ManyToOne
    private User leader;

    @ManyToMany
    private List<User> members = new ArrayList<>();

    @Enumerated(STRING)
    private StudyStatus status = OPENED;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    public static int MIN_MEMBERS_SIZE = 1;

    private Study(String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer, User leader) {
        this.name = name;
        this.info = info;
        this.maxMembersSize = maxMembersSize;
        this.difficulty = difficulty;
        this.period = period;
        this.needCareer = needCareer;
        this.leader = leader;
        this.members.add(leader);
    }

    public static Study create(String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer, User leader) {
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(info));
        require(maxMembersSize >= MIN_MEMBERS_SIZE);
        require(nonNull(difficulty));
        require(nonNull(period));
        require(nonNull(needCareer));
        require(nonNull(leader));

        return new Study(name, info, maxMembersSize, difficulty, period, needCareer, leader);
    }
}
