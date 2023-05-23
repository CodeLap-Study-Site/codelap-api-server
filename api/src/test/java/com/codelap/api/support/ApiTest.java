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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;

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

    protected void setMockMvcPerform(HttpMethod httpMethod, Object req, String... urlInfo) throws Exception {
        String url = urlInfo[0];
        String identifier = urlInfo.length == 1 ? urlInfo[0].substring(1) : urlInfo[1];

        switch (httpMethod) {
            case GET -> setMockMvcPerform(MockMvcRequestBuilders.get(url), (ResultMatcher) req, identifier);

            case POST -> setMockMvcPerform(MockMvcRequestBuilders.post(url), req, identifier);

            case DELETE -> setMockMvcPerform(MockMvcRequestBuilders.delete(url), req, identifier);

            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
    }

    protected void setMultipartFileMockMvcPerform(HttpMethod httpMethod, List<MockMultipartFile> multipartFiles, String... urlInfo) throws Exception {
        String url = urlInfo[0];
        String identifier = urlInfo.length == 1 ? urlInfo[0].substring(1) : urlInfo[1];

        switch (httpMethod) {
            case POST -> setMultipartFileMockMvcPerform(MockMvcRequestBuilders.multipart(url), multipartFiles, identifier);

            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
    }

    protected void setMultipartFileMockMvcPerform(HttpMethod httpMethod, List<MockMultipartFile> multipartFiles, MultiValueMap<String, String> idMap, String... urlInfo) throws Exception {
        String url = urlInfo[0];
        String identifier = urlInfo.length == 1 ? urlInfo[0].substring(1) : urlInfo[1];

        switch (httpMethod) {
            case POST -> setMultipartFileMockMvcPerform((MockMultipartHttpServletRequestBuilder) MockMvcRequestBuilders.multipart(url).params(idMap),multipartFiles, identifier);

            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
    }

    protected void setMockMvcPerform(HttpMethod httpMethod, String... urlInfo) throws Exception {
        String url = urlInfo[0];
        String identifier = urlInfo.length == 1 ? urlInfo[0].substring(1) : urlInfo[1];

        switch (httpMethod) {
            case POST -> setMockMvcPerform(MockMvcRequestBuilders.post(url), identifier);

            case DELETE -> setMockMvcPerform(MockMvcRequestBuilders.delete(url), identifier);

            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
    }

    protected void setMockMvcPerform(HttpMethod httpMethod, MultiValueMap<String, String> params, ResultMatcher[] matchers, String url) throws Exception {
        String identifier = url.substring(1);

        switch (httpMethod) {
            case GET -> setMockMvcPerform(MockMvcRequestBuilders.get(url), params, matchers, identifier);

            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        }
    }

    private RestDocumentationResultHandler restDocsSet(String url) {
        return document(url,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()));
    }

    private ResultActions setMockMvcPerform(MockHttpServletRequestBuilder method, Object req, String identifier) throws Exception {
        return this.mockMvc.perform(method
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(status().isOk())
                .andDo(restDocsSet(identifier));
    }

    private ResultActions setMockMvcPerform(MockHttpServletRequestBuilder method, String identifier) throws Exception {
        return this.mockMvc.perform(method)
                .andExpectAll(status().isOk())
                .andDo(restDocsSet(identifier));
    }

    private ResultActions setMockMvcPerform(MockHttpServletRequestBuilder method, MultiValueMap<String, String> params, ResultMatcher[] matchers, String identifier) throws Exception {
        return this.mockMvc.perform(method
                        .params(params))
                .andExpect(status().isOk())
                .andExpectAll(matchers)
                .andDo(restDocsSet(identifier));
    }

    private ResultActions setMockMvcPerform(MockHttpServletRequestBuilder method, ResultMatcher matcher, String identifier) throws Exception {
        return this.mockMvc.perform(method)
                .andExpect(status().isOk())
                .andExpectAll(matcher)
                .andDo(restDocsSet(identifier));
    }

    private ResultActions setMultipartFileMockMvcPerform(MockMultipartHttpServletRequestBuilder method, List<MockMultipartFile> multipartFile, String identifier) throws Exception {
        for (MockMultipartFile mockMultipartFile : multipartFile) {
            method.file(mockMultipartFile);
        }
        return this.mockMvc.perform(method)
                .andExpect(status().isOk())
                .andDo(restDocsSet(identifier));
    }

    protected List<MockMultipartFile> getMultipartFiles(MockMultipartFile... multipartFiles) {
        List<MockMultipartFile> files = new ArrayList<>();
        for (MockMultipartFile multipartFile : multipartFiles) {
            files.add(multipartFile);
        }

        return files;
    }
}
