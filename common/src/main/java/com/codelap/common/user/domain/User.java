package com.codelap.common.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import static com.codelap.common.support.Preconditions.require;
import static jakarta.persistence.GenerationType.IDENTITY;
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

    public static int MIN_AGE = 0;

    private User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static User create(String name, int age) {
        require(Strings.isNotBlank(name));
        require(age > MIN_AGE);

        return new User(name, age);
    }
}
