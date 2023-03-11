package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequest;
import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequestUserCareerDto;
import com.codelap.api.controller.user.dto.UserUpdateDto;
import com.codelap.api.support.ApiTest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codelap.api.controller.user.dto.UserUpdateDto.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends ApiTest {

    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        UserCareer career = UserCareer.create("직무", 1);
        user = userRepository.save(User.create("name", 10, career, "abcd"));
    }


    @Test
    void 유저_생성_성공() throws Exception {
        UserCreateRequestUserCareerDto dto = new UserCreateRequestUserCareerDto("직무", 10);

        UserCreateRequest req = new UserCreateRequest("name", 10,"abcd", dto);

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

    @Test
    void 유저_수정_성공() throws Exception {
        UserUpdateRequestUserCareerDto dto = new UserUpdateRequestUserCareerDto("직무", 10);

        UserUpdateRequest req = new UserUpdateRequest(user.getId(), "updatedName", 11, dto);

        mockMvc.perform(post("/user/update")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("user/update",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}