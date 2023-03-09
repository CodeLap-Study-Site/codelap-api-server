package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequest;
import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequestUserCareerDto;
import com.codelap.api.support.ApiTest;
import org.junit.jupiter.api.Test;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends ApiTest {


    @Test
    void 유저_생성_성공() throws Exception {
        UserCreateRequestUserCareerDto dto = new UserCreateRequestUserCareerDto("직무", 10);

        UserCreateRequest req = new UserCreateRequest("name", 10, dto);

        mockMvc.perform(post("/user")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("user/create",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}