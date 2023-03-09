package com.codelap.common.user.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;

import static com.codelap.common.support.Preconditions.require;
import static com.codelap.common.user.domain.UserStatus.CREATED;
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

    private int age;

    @Embedded
    private UserCareer career;

    private UserStatus status = CREATED;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    public static int MIN_AGE = 0;

    private User(String name, int age, UserCareer career) {
        this.name = name;
        this.age = age;
        this.career = career;
    }

    public static User create(String name, int age, UserCareer career) {
        require(Strings.isNotBlank(name));
        require(age > MIN_AGE);
        require(nonNull(career));

        return new User(name, age, career);
    }
}
