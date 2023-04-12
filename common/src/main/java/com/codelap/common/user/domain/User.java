package com.codelap.common.user.domain;

import com.codelap.common.study.domain.Study;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    private final List<Study> studies = new ArrayList<>();

    @Embedded
    private UserCareer career;

    @Setter
    private UserStatus status = CREATED;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    public static int MIN_AGE = 0;

    private User(String name, int age, UserCareer career, String password, String email) {
        this.name = name;
        this.age = age;
        this.career = career;
        this.password = password;
        this.email = email;
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
