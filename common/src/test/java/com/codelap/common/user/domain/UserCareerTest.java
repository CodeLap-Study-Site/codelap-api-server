package com.codelap.common.user.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.codelap.common.user.domain.UserCareer.MIN_YEAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class UserCareerTest {

    @Test
    void 유저_경력_생성() {
        UserCareer career = UserCareer.create("직무", 10);

        assertThat(career.getOccupation()).isEqualTo("직무");
        assertThat(career.getYear()).isEqualTo(10);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_경력_생성_실패__직무가_널이나_공백(String occupation) {
        assertThatIllegalArgumentException().isThrownBy(() -> UserCareer.create(occupation, 10));
    }

    @Test
    void 유저_경력_생성_실패__년차가_최소값_미만() {
        assertThatIllegalArgumentException().isThrownBy(() -> UserCareer.create("직무", MIN_YEAR - 1));
    }
}
