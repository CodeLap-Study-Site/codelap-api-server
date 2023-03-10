package com.codelap.common.user.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

import java.time.OffsetDateTime;

import static com.codelap.common.support.Preconditions.*;
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

    private String password;

    @Embedded
    private UserCareer career;

    @Setter
    private UserStatus status = CREATED;

    private final OffsetDateTime createdAt = OffsetDateTime.now();

    public static int MIN_AGE = 0;

    private User(String name, int age, UserCareer career, String password) {
        this.name = name;
        this.age = age;
        this.career = career;
        this.password = password;
    }

    public static User create(String name, int age, UserCareer career, String password, String checkPassword) {
        require(Strings.isNotBlank(name));
        require(age > MIN_AGE);
        require(nonNull(career));
        require(Strings.isNotBlank(password));
        // 패스워드와, 재입력한 패스워드가 같은지 확인하는 메서드가 필요.
        passwordCheck(password, checkPassword);


        return new User(name, age, career, password);
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
}
