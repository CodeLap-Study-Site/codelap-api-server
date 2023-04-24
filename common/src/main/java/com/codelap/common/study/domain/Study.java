package com.codelap.common.study.domain;

import com.codelap.common.bookmark.domain.Bookmark;
import com.codelap.common.studyComment.domain.StudyComment;
import com.codelap.common.studyView.domain.StudyView;
import com.codelap.common.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.codelap.common.study.domain.StudyStatus.*;
import static com.codelap.common.support.ErrorCode.*;
import static com.codelap.common.support.Preconditions.*;
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

    @ElementCollection
    @Enumerated(STRING)
    private List<TechStack> techStackList;

    @Enumerated(STRING)
    private StudyDifficulty difficulty;

    @Embedded
    private StudyPeriod period;

    @Embedded
    private StudyNeedCareer needCareer;

    @ManyToOne
    private User leader;

    @ManyToMany
    private final List<User> members = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    private final List<StudyComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    private final List<StudyView> views = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    private final List<Bookmark> bookmarks = new ArrayList<>();

    @Setter
    @Enumerated(STRING)
    private StudyStatus status = OPENED;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    public static int MIN_MEMBERS_SIZE = 1;

    public boolean isLeader(User leader) {
        return this.leader == leader;
    }

    public boolean containsMember(User user) {
        return this.members.contains(user);
    }

    public boolean emptyMember() {
        return this.members.size() == 1;
    }

    private Study(String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer, User leader, List<TechStack> techStackList) {
        this.name = name;
        this.info = info;
        this.maxMembersSize = maxMembersSize;
        this.difficulty = difficulty;
        this.period = period;
        this.needCareer = needCareer;
        this.leader = leader;
        this.members.add(leader);
        this.techStackList = techStackList;
    }

    public static Study create(String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer, User leader, List<TechStack> techStackList) {
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(info));
        require(nonNull(difficulty));
        require(nonNull(period));
        require(nonNull(needCareer));
        require(nonNull(leader));
        require(nonNull(techStackList));
        validate(maxMembersSize >= MIN_MEMBERS_SIZE, INVALID_MEMBER_SIZE);

        return new Study(name, info, maxMembersSize, difficulty, period, needCareer, leader, techStackList);
    }

    public void update(String name, String info, int maxMembersSize, StudyDifficulty difficulty, StudyPeriod period, StudyNeedCareer needCareer, List<TechStack> techStackList) {
        require(Strings.isNotBlank(name));
        require(Strings.isNotBlank(info));
        require(nonNull(difficulty));
        require(nonNull(period));
        require(nonNull(needCareer));
        require(nonNull(techStackList));

        check(status != DELETED);

        validate(maxMembersSize >= MIN_MEMBERS_SIZE, INVALID_MEMBER_SIZE);

        this.name = name;
        this.info = info;
        this.maxMembersSize = maxMembersSize;
        this.difficulty = difficulty;
        this.period = period;
        this.needCareer = needCareer;
        this.techStackList = techStackList;
    }

    public void addMember(User user) {
        validate(!containsMember(user), ALREADY_EXISTED_MEMBER);
        validate(maxMembersSize > members.size(), INVALID_MEMBER_SIZE);

        check(status != DELETED);

        members.add(user);
    }

    public void addComment(StudyComment studyComment) {
        comments.add(studyComment);
    }

    public void addView(StudyView studyView) {
        views.add(studyView);
    }

    public void addBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
    }

    public void removeBookmark(Bookmark bookmark){
       bookmarks.remove(bookmark);
    }

    public void changeLeader(User user) {
        validate(containsMember(user), NOT_EXISTED_MEMBER);
        validate(!isLeader(user), NOT_ALLOWED_AS_LEADER);

        check(status != DELETED);

        this.leader = user;
    }

    public void proceed() {
        check(status == OPENED);

        this.status = IN_PROGRESS;
    }

    public void open(StudyPeriod period) {
        require(nonNull(period));

        check(CAN_OPEN_STATUSES.contains(status));

        this.period = period;
        this.status = OPENED;
    }

    public void removeMember(User member) {
        validate(containsMember(member), NOT_EXISTED_MEMBER);
        validate(!isLeader(member), NOT_ALLOWED_AS_LEADER);

        check(status != DELETED);

        members.remove(member);
    }

    public void close() {
        check(CAN_CLOSED_STATUSES.contains(status));

        this.status = CLOSED;
    }

    public void leave(User member) {
        validate(containsMember(member), NOT_EXISTED_MEMBER);
        validate(!isLeader(member), NOT_ALLOWED_AS_LEADER);

        members.remove(member);
    }

    public void delete() {
        validate(emptyMember(), ANOTHER_EXISTED_MEMBER);

        check(CAN_DELETE_STATUSES.contains(status));

        this.status = DELETED;
    }
}
