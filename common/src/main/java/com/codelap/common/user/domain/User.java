package com.codelap.common.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;
import java.util.List;

import static com.codelap.common.support.Preconditions.check;
import static com.codelap.common.support.Preconditions.require;
import static com.codelap.common.user.domain.UserStatus.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private Long socialId;

    @Embedded
    private UserCareer career = new UserCareer();

    @ElementCollection
    private List<UserTechStack> techStacks;

    @Setter
    @Enumerated(STRING)
    private UserStatus status = CREATED;

    @ElementCollection
    private List<UserFile> files;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    public boolean isActivated() {
        return this.status == ACTIVATED;
    }

    public User(Long socialId) {
        this.socialId = socialId;
    }

    public static User create(Long socialId) {
        return new User(socialId);
    }

    public void activate(String name, UserCareer career, List<UserTechStack> techStacks) {
        check(status == CREATED);

        this.status = ACTIVATED;

        update(name, career, techStacks);
    }

    public void update(String name, UserCareer career, List<UserTechStack> techStacks) {
        require(Strings.isNotBlank(name));
        require(nonNull(career));
        require(nonNull(techStacks));

        check(status == ACTIVATED);

        this.name = name;
        this.career = career;
        this.techStacks = techStacks;
    }

    public void changeImage(List<UserFile> files) {
        require(nonNull(files));

        this.files = files;
    }

    public void delete() {
        check(status != DELETED);

        this.status = DELETED;
    }
}
