package com.codelap.common.study.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Objects;

import static com.codelap.common.support.Preconditions.require;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.PROTECTED;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
public class StudyPeriod {

    private OffsetDateTime startAt;
    private OffsetDateTime endAt;

    private StudyPeriod(OffsetDateTime startAt, OffsetDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public static StudyPeriod create(OffsetDateTime startAt, OffsetDateTime endAt) {
        require(nonNull(startAt));
        require(nonNull(endAt));

        return new StudyPeriod(startAt, endAt);
    }
}
