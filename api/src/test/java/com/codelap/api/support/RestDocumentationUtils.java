package com.codelap.api.support;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.util.List;

import static com.codelap.api.support.RestDocumentationUtils.ParameterType.PATH_VARIABLE;
import static com.codelap.api.support.RestDocumentationUtils.ParameterType.QUERY_PARAMETER;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public class RestDocumentationUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final MediaType DEFAULT_CONTENT_TYPE = APPLICATION_JSON;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    public static MockHttpServletRequestBuilder postMethodRequestBuilder(
            String url,
            MediaType contentType,
            Object content,
            String authorizationToken,
            Object... pathVariables
    ) throws Exception {
        MockHttpServletRequestBuilder request = post(url, pathVariables);

        if (content != null) {
            byte[] contentBytes = objectMapper.writeValueAsBytes(content);
            request
                    .contentType(contentType != null ? contentType : DEFAULT_CONTENT_TYPE)
                    .content(contentBytes);
        }

        if (authorizationToken != null) {
            request.header(AUTHORIZATION_HEADER, "Bearer " + authorizationToken);
        }

        return request;
    }

    public static MockHttpServletRequestBuilder getMethodRequestBuilder(
            String url,
            String authorizationToken,
            List<Pair<String, String>> requestParameters,
            Object... pathVariables
    ) {
        MockHttpServletRequestBuilder request = get(url, pathVariables);

        if (requestParameters != null) {
            for (Pair<String, String> requestParameter : requestParameters) {
                request.param(requestParameter.getFirst(), requestParameter.getSecond());
            }
        }

        if (authorizationToken != null) {
            request.header(AUTHORIZATION_HEADER, "Bearer " + authorizationToken);
        }

        return request;
    }

    public static MockHttpServletRequestBuilder getMethodRequestBuilder(
            String url,
            MediaType contentType,
            Object content,
            String authorizationToken,
            Object... pathVariables
    ) throws Exception {
        MockHttpServletRequestBuilder request = get(url, pathVariables);

        if (content != null) {
            byte[] contentBytes = objectMapper.writeValueAsBytes(content);
            request
                    .contentType(contentType != null ? contentType : DEFAULT_CONTENT_TYPE)
                    .content(contentBytes);
        }

        if (authorizationToken != null) {
            request.header(AUTHORIZATION_HEADER, "Bearer " + authorizationToken);
        }

        return request;
    }

    public static MockHttpServletRequestBuilder deleteMethodRequestBuilder(
            String url,
            MediaType contentType,
            Object content,
            String authorizationToken,
            Object... pathVariables) throws Exception {
        MockHttpServletRequestBuilder request = delete(url, pathVariables);

        if (content != null) {
            byte[] contentBytes = objectMapper.writeValueAsBytes(content);
            request
                    .contentType(contentType != null ? contentType : DEFAULT_CONTENT_TYPE)
                    .content(contentBytes);
        }

        if (authorizationToken != null) {
            request.header(AUTHORIZATION_HEADER, "Bearer " + authorizationToken);
        }

        return request;
    }

    public static MockHttpServletRequestBuilder multipartRequestBuilder(
            String url,
            List<MockMultipartFile> multipartFile,
            String authorizationToken,
            Object... pathVariables
    ) {
        MockMultipartHttpServletRequestBuilder request = multipart(url, pathVariables);

        for (MockMultipartFile mockMultipartFile : multipartFile) {
            request.file(mockMultipartFile);
        }

        if (authorizationToken != null) {
            request.header(AUTHORIZATION_HEADER, "Bearer " + authorizationToken);
        }

        return request;
    }

    public static RestDocumentationResultHandler getRestDocumentationResult(
            String identifier,
            String tag,
            String description,
            List<Pair<String, String>> requestParameters,
            List<Pair<String, String>> pathParameters) {
        ResourceSnippetParametersBuilder resourceSnippetParametersBuilder = new ResourceSnippetParametersBuilder()
                .tags(tag)
                .description(description);

        setParameters(QUERY_PARAMETER, requestParameters, resourceSnippetParametersBuilder);
        setParameters(PATH_VARIABLE, pathParameters, resourceSnippetParametersBuilder);

        return document(
                identifier,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                resource(resourceSnippetParametersBuilder.build())
        );
    }

    private static void setParameters(
            ParameterType parameterType,
            List<Pair<String, String>> parameterWithNames,
            ResourceSnippetParametersBuilder resourceSnippetParametersBuilder) {
        if (parameterWithNames != null) {
            ParameterDescriptor[] parameters = parameterWithNames.stream()
                    .map(parameterWithName -> parameterWithName(parameterWithName.getFirst())
                            .description(parameterWithName.getSecond()))
                    .toArray(ParameterDescriptor[]::new);

            switch (parameterType) {
                case QUERY_PARAMETER -> resourceSnippetParametersBuilder.queryParameters(parameters);
                case PATH_VARIABLE -> resourceSnippetParametersBuilder.pathParameters(parameters);
            }
        }
    }

    enum ParameterType {
        QUERY_PARAMETER, PATH_VARIABLE
    }
}
