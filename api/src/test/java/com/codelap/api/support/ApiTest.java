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
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static com.codelap.fixture.UserFixture.createActivateUser;
import static com.codelap.fixture.UserFixture.createUser;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(RestDocumentationExtension.class)
public abstract class ApiTest {

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

    protected RestDocumentationResultHandler restDocsSet(String url) {
        return document(url,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));
    }

    protected ResultActions setMockMvcPerform(HttpMethod httpMethod, Object req, String url) throws Exception {
        String identifier = url.substring(1);

        switch (httpMethod) {
            case POST -> setMockMvcPerform(MockMvcRequestBuilders.post(url), req, identifier);

            case DELETE -> setMockMvcPerform(MockMvcRequestBuilders.delete(url), req, identifier);
        }
        return null;
    }

    protected ResultActions setMockMvcPerform(HttpMethod httpMethod, Object req, String url, String identifier) throws Exception {
        switch (httpMethod) {
            case POST -> setMockMvcPerform(MockMvcRequestBuilders.post(url), req, identifier);

            case DELETE -> setMockMvcPerform(MockMvcRequestBuilders.delete(url), req, identifier);
        }
        return null;
    }

    protected ResultActions setMockMvcPerform(HttpMethod httpMethod, MultiValueMap<String, String> params, ResultMatcher[] matchers, String url) throws Exception {
        String identifier = url.substring(1);

        if (httpMethod == HttpMethod.GET) {
            setMockMvcPerform(MockMvcRequestBuilders.get(url), params, matchers, identifier);
        }
        return null;
    }

    protected ResultActions setMockMvcPerform(MockHttpServletRequestBuilder method, Object req, String identifier) throws Exception {
        return this.mockMvc.perform(method
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk())
                .andDo(restDocsSet(identifier));
    }

    protected ResultActions setMockMvcPerform(MockHttpServletRequestBuilder method, MultiValueMap<String, String> params, ResultMatcher[] matchers, String identifier) throws Exception {
        return this.mockMvc.perform(method
                        .params(params))
                .andExpect(status().isOk())
                .andExpectAll(matchers)
                .andDo(restDocsSet(identifier));
    }
}
