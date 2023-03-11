package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserChangePasswordDto;
import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequest;
import com.codelap.api.controller.user.dto.UserCreateDto.UserCreateRequestUserCareerDto;
import com.codelap.api.controller.user.dto.UserDeleteDto;
import com.codelap.api.controller.user.dto.UserUpdateDto;
import com.codelap.api.support.ApiTest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserCareer;
import com.codelap.common.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codelap.api.controller.user.dto.UserChangePasswordDto.*;
import static com.codelap.api.controller.user.dto.UserDeleteDto.*;
import static com.codelap.api.controller.user.dto.UserUpdateDto.*;
import static com.codelap.common.user.domain.UserStatus.CREATED;
import static com.codelap.common.user.domain.UserStatus.DELETED;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

        User foundUser = userRepository.findAll().get(1);

        assertThat(foundUser.getId()).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("name");
        assertThat(foundUser.getAge()).isEqualTo(10);
        assertThat(foundUser.getPassword()).isEqualTo("abcd");
        assertThat(foundUser.getCareer().getOccupation()).isEqualTo(dto.occupation());
        assertThat(foundUser.getCareer().getYear()).isEqualTo(dto.year());
        assertThat(foundUser.getStatus()).isEqualTo(CREATED);
        assertThat(foundUser.getCreatedAt()).isNotNull();

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

        assertThat(user.getName()).isEqualTo("updatedName");
        assertThat(user.getAge()).isEqualTo(11);
        assertThat(user.getCareer().getOccupation()).isEqualTo(dto.occupation());
        assertThat(user.getCareer().getYear()).isEqualTo(dto.year());
    }

    @Test
    void 유저_비밀번호_수정_성공() throws Exception{
        UserChangePasswordRequest req = new UserChangePasswordRequest(user.getId(), user.getPassword(), "newPassword");

        mockMvc.perform(post("/user/change-password")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("user/change-password",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(user.getPassword()).isEqualTo("newPassword");
    }

    @Test
    void 유저_삭제_성공() throws Exception{
        UserDeleteRequest req = new UserDeleteRequest(user.getId());

        mockMvc.perform(delete("/user")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("user/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(user.getStatus()).isEqualTo(DELETED);
    }
}