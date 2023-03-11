package com.codelap.common.study.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import static com.codelap.common.support.Preconditions.require;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyNeedCareer {
    private String occupation;
    private int year;

    private StudyNeedCareer(String occupation, int year) {
        this.occupation = occupation;
        this.year = year;
    }

    public static final int MIN_YEAR = 0;

    public static StudyNeedCareer create(String occupation, int year) {
        require(Strings.isNotBlank(occupation));
        require(year >= MIN_YEAR);

        return new StudyNeedCareer(occupation, year);
    }
}
