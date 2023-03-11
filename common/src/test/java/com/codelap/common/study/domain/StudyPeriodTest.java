package com.codelap.common.study.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class StudyPeriodTest {
    @Test
    void 스터디_기간_생성_성공() {
        StudyPeriod studyPeriod = StudyPeriod.create(OffsetDateTime.now(), OffsetDateTime.now().plusMinutes(10));

        assertThat(studyPeriod.getStartAt()).isNotNull();
        assertThat(studyPeriod.getEndAt()).isNotNull();

    }

    @Test
    void 스터디_기간_생성_실패__시작날짜가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyPeriod.create(null, OffsetDateTime.now().plusMinutes(10)));
    }

    @Test
    void 스터디_기간_생성_실패__종료날짜가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> StudyPeriod.create(OffsetDateTime.now(), null));
    }
}