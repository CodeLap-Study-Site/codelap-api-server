package com.codelap.common.user.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.codelap.common.user.domain.UserStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class UserTest {

    @Test
    void 유저_생성_성공() {
        UserCareer career = UserCareer.create("직무", 1);

        var user = User.create("name", 10, career);

        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getAge()).isEqualTo(10);
        assertThat(user.getCareer()).isSameAs(career);
        assertThat(user.getStatus()).isSameAs(CREATED);
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_생성_실패__이름이_공백_혹은_널(String name) {
        UserCareer career = UserCareer.create("직무", 1);

        assertThatIllegalArgumentException().isThrownBy(() -> User.create(name, 10, career));
    }

    @Test
    void 유저_생성_실패__나이가_최소값_보다_작음() {
        UserCareer career = UserCareer.create("직무", 1);

        assertThatIllegalArgumentException().isThrownBy(() -> User.create("name", User.MIN_AGE - 1, career));
    }

    @Test
    void 유저_생성_실패__직무가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> User.create("name", 10, null));
    }
}
