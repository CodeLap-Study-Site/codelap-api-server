package com.codelap.common.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.codelap.common.user.domain.User.MIN_AGE;
import static com.codelap.common.user.domain.UserStatus.CREATED;
import static com.codelap.common.user.domain.UserStatus.DELETED;
import static org.assertj.core.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        user = User.create("name", 10, career);
    }

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

        assertThatIllegalArgumentException().isThrownBy(() -> User.create("name", MIN_AGE - 1, career));
    }

    @Test
    void 유저_생성_실패__직무가_널() {
        assertThatIllegalArgumentException().isThrownBy(() -> User.create("name", 10, null));
    }

    @Test
    void 유저_수정_성공() {
        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        user.update("updateName", 11, updateCareer);

        assertThat(user.getName()).isEqualTo("updateName");
        assertThat(user.getAge()).isEqualTo(11);
        assertThat(user.getCareer()).isSameAs(updateCareer);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_수정_실패__이름이_널이거나_공백(String updateName) {
        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        assertThatIllegalArgumentException().isThrownBy(() ->
                user.update(updateName, 11, updateCareer)
        );
    }

    @Test
    void 유저_수정_실패__나이가_최소값_미만() {
        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        assertThatIllegalArgumentException().isThrownBy(() ->
                user.update("updatedName", MIN_AGE - 1, updateCareer)
        );
    }

    @Test
    void 유저_수정_실패__직무가_널() {
        assertThatIllegalArgumentException().isThrownBy(() ->
                user.update("updatedName", 11, null)
        );
    }

    @Test
    void 유저_수정_실패__생성됨_상태가_아님() {
        user.setStatus(DELETED);

        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        assertThatIllegalStateException().isThrownBy(() ->
                user.update("updatedName", 11, updateCareer)
        );
    }
}
