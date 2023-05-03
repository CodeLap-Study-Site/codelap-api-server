package com.codelap.common.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;

import static com.codelap.common.support.Preconditions.check;
import static com.codelap.common.support.Preconditions.require;
import static com.codelap.common.user.domain.UserStatus.CREATED;
import static com.codelap.common.user.domain.UserStatus.DELETED;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uq_email", columnNames = "email"),
})
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private int age;

    private Long socialId;

    @Embedded
    private UserCareer career = new UserCareer();

    @Setter
    private UserStatus status = CREATED;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    public static int MIN_AGE = 0;

    public User(Long socialId) {
        this.socialId = socialId;
    }

    private User(String name, int age, UserCareer career, String password, String email) {
        this.name = name;
        this.age = age;
        this.career = career;
        this.password = password;
        this.email = email;
    }

    public static User create(Long socialId) {

        return new User(socialId);
    }

    public static User create(String name, int age, UserCareer career, String password, String email) {
        require(Strings.isNotBlank(name));
        require(age > MIN_AGE);
        require(nonNull(career));
        require(Strings.isNotBlank(password));
        require(Strings.isNotBlank(email));

        return new User(name, age, career, password, email);
    }

    public void update(String name, int age, UserCareer career) {
        require(Strings.isNotBlank(name));
        require(age > MIN_AGE);
        require(nonNull(career));

        check(status == CREATED);

        this.name = name;
        this.age = age;
        this.career = career;
    }

    public void changePassword(String password, String newPassword) {
        require(Strings.isNotBlank(password));
        require(Strings.isNotBlank(newPassword));

        check(status == CREATED);

        require(this.password.equals(password));
        require(!this.password.equals(newPassword));

        this.password = newPassword;
    }

    public void delete() {
        check(status != DELETED);

        this.status = DELETED;
    }
}
