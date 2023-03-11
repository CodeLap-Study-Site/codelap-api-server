package com.codelap.common.study.domain;

import com.codelap.common.user.domain.UserCareer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;


import static com.codelap.common.study.domain.StudyNeedCareer.*;
import static com.codelap.common.user.domain.UserCareer.MIN_YEAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;

class StudyNeedCareerTest {
    @Test
    void 필요경력_생성_성공() {
        StudyNeedCareer needCareer = create("직무", 10);

        assertThat(needCareer.getOccupation()).isEqualTo("직무");
        assertThat(needCareer.getYear()).isEqualTo(10);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 필요경력_생성_실패__직무가_널이나_공백(String occupation) {
        assertThatIllegalArgumentException().isThrownBy(() -> create(occupation, 10));
    }

    @Test
    void 필요경력_생성_실패__경력이_최소값_미만() {
        assertThatIllegalArgumentException().isThrownBy(() -> create("직무", MIN_YEAR - 1));
    }
}