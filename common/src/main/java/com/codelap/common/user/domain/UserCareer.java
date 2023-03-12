package com.codelap.common.user.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import static com.codelap.common.support.Preconditions.require;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCareer {

    private String occupation;
    private int year;

    private UserCareer(String occupation, int year) {
        this.occupation = occupation;
        this.year = year;
    }

    public static final int MIN_YEAR = 0;

    public static UserCareer create(String occupation, int year) {
        require(Strings.isNotBlank(occupation));
        require(year >= MIN_YEAR);

        return new UserCareer(occupation, year);
    }
}
