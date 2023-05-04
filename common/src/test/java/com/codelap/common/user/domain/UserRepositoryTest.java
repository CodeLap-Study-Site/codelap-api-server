package com.codelap.common.user.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.codelap.common.user.domain.UserStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저_생성_성공() {
        User user = User.create(10L);

        user = userRepository.save(user);

        assertThat(user.getId()).isNotNull();
        assertThat(user.getSocialId()).isEqualTo(10L);
        assertThat(user.getStatus()).isEqualTo(CREATED);
        assertThat(user.getCreatedAt()).isNotNull();
    }
}