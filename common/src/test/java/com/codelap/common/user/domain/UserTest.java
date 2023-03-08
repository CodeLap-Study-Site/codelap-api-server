package com.codelap.common.user.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class UserTest {

    @Test
    void 유저_생성_성공() {
        var user = User.create("name", 10);

        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getAge()).isEqualTo(10);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_생성_실패__이름이_공백_혹은_널(String name) {
        assertThatIllegalArgumentException().isThrownBy(() -> User.create(name, 10));
    }

    @Test
    void 유저_생성_실패__나이가_최소값_보다_작음() {
        assertThatIllegalArgumentException().isThrownBy(() -> User.create("name", User.MIN_AGE - 1));
    }
}
