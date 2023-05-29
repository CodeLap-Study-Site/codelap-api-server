package com.codelap.api.support;

import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.codelap.fixture.UserFixture.createActivateUser;
import static com.codelap.fixture.UserFixture.createUser;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public abstract class BasicApiTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
                .build();
    }

    protected User prepareLoggedInActiveUser() {
        User activateUser = createActivateUser();

        return prepareLoggedInUserInternal(activateUser);
    }

    protected User prepareLoggedInUser() {
        User user = createUser();

        return prepareLoggedInUserInternal(user);
    }

    private User prepareLoggedInUserInternal(User user) {
        user = userRepository.save(user);

        userRepository.flush();

        login(user.getId());

        return user;
    }

    protected void login(User user) {
        login(user.getId());
    }

    private void login(Long id) {
        DefaultCodeLapUser codeLapUser = (DefaultCodeLapUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        codeLapUser.setId(id);
    }
}
