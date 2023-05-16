package com.codelap.common.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static com.codelap.common.support.TechStack.Go;
import static com.codelap.common.user.domain.UserStatus.*;
import static com.codelap.fixture.UserFixture.createActivateUser;
import static com.codelap.fixture.UserFixture.createUser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = createActivateUser();
    }

    @Test
    void 유저_생성_성공() {
        User user = User.create(10L);

        assertThat(user.getSocialId()).isEqualTo(10L);
        assertThat(user.getStatus()).isEqualTo(CREATED);
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    void 유저_활성화_성공() {
        User user = createUser();

        UserTechStack techStack = new UserTechStack(Go);
        UserCareer career = UserCareer.create("직무", 2);

        user.activate("name", career, List.of(techStack));

        assertThat(user.getStatus()).isEqualTo(ACTIVATED);
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getCareer()).isSameAs(career);
        assertThat(user.getTechStacks()).containsExactly(techStack);
    }

    @ParameterizedTest
    @EnumSource(value = UserStatus.class, names = {"CREATED"}, mode = EXCLUDE)
    void 유저_활성화_실패__생성됨_상태가_아님(UserStatus status) {
        user.setStatus(status);

        UserTechStack techStack = new UserTechStack(Go);
        UserCareer career = UserCareer.create("직무", 2);

        assertThatIllegalStateException().isThrownBy(() ->
                user.activate("name", career, List.of(techStack))
        );
    }

    @Test
    void 유저_수정_성공() {
        user.setStatus(ACTIVATED);

        UserTechStack userTechStack = new UserTechStack(Go);
        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        user.update("updateName", updateCareer, List.of(userTechStack));

        assertThat(user.getName()).isEqualTo("updateName");
        assertThat(user.getCareer()).isSameAs(updateCareer);
        assertThat(user.getTechStacks()).containsExactly(userTechStack);
    }

    @Test
    void 유저_수정_성공2() {
        user.setStatus(ACTIVATED);

        UserTechStack userTechStack = new UserTechStack(Go);
        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        UserFile file = (UserFile) UserFile.create();
        file = file.update("savedName", "originalName", 100L);

        user.update("updateName", updateCareer, List.of(userTechStack), List.of(file));

        assertThat(user.getName()).isEqualTo("updateName");
        assertThat(user.getCareer()).isSameAs(updateCareer);
        assertThat(user.getTechStacks()).containsExactly(userTechStack);
        assertThat(user.getFiles()).contains(file);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 유저_수정_실패__이름이_널이거나_공백(String updateName) {
        UserTechStack userTechStack = new UserTechStack(Go);
        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        assertThatIllegalArgumentException().isThrownBy(() ->
                user.update(updateName, updateCareer, List.of(userTechStack))
        );
    }

    @Test
    void 유저_수정_실패__직무가_널() {
        UserTechStack userTechStack = new UserTechStack(Go);

        assertThatIllegalArgumentException().isThrownBy(() ->
                user.update("updateName", null, List.of(userTechStack))
        );
    }

    @Test
    void 유저_수정_실패__기술_스택이_널() {
        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        assertThatIllegalArgumentException().isThrownBy(() ->
                user.update("updateName", updateCareer, null)
        );
    }

    @ParameterizedTest
    @EnumSource(value = UserStatus.class, names = {"ACTIVATED"}, mode = EXCLUDE)
    void 유저_수정_실패__활성화_상태가_아님(UserStatus status) {
        user.setStatus(status);

        UserTechStack userTechStack = new UserTechStack(Go);
        UserCareer updateCareer = UserCareer.create("업데이트된 직무", 2);

        assertThatIllegalStateException().isThrownBy(() ->
                user.update("updatedName", updateCareer, List.of(userTechStack))
        );
    }

    @Test
    void 유저_삭제_성공() {
        user.delete();

        assertThat(user.getStatus()).isEqualTo(DELETED);
    }

    @Test
    void 유저_삭제_실패__이미_삭제됨() {
        user.delete();

        assertThatIllegalStateException().isThrownBy(() -> user.delete());
    }
}
