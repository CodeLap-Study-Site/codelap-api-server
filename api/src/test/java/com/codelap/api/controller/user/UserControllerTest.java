package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserActivateDto.UserActivateRequest;
import com.codelap.api.controller.user.dto.UserActivateDto.UserActivateRequestUserCareerDto;
import com.codelap.api.support.ApiTest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserRepository;
import com.codelap.common.user.domain.UserTechStack;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequest;
import static com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequestUserCareerDto;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.user.domain.UserStatus.ACTIVATED;
import static com.codelap.common.user.domain.UserStatus.DELETED;
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


    @Test
    @WithUserDetails
    void 유저_활성화_성공() throws Exception {
        User user = prepareLoggedInUser();

        UserTechStack techStack = new UserTechStack(Java);
        UserActivateRequestUserCareerDto dto = new UserActivateRequestUserCareerDto("직무", 10);

        UserActivateRequest req = new UserActivateRequest("name", dto, List.of(techStack));

        mockMvc.perform(post("/user/activate")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("user/activate",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(user.getStatus()).isEqualTo(ACTIVATED);
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getCareer().getOccupation()).isEqualTo(dto.occupation());
        assertThat(user.getCareer().getYear()).isEqualTo(dto.year());
        assertThat(user.getTechStacks().stream().map(UserTechStack::getTechStack))
                .containsExactly(techStack.getTechStack());
    }

    @Test
    @WithUserDetails
    void 유저_수정_성공() throws Exception {
        User user = prepareLoggedInActiveUser();

        UserTechStack techStack = new UserTechStack(Java);
        UserUpdateRequestUserCareerDto dto = new UserUpdateRequestUserCareerDto("직무", 10);

        UserUpdateRequest req = new UserUpdateRequest("updatedName", dto, List.of(techStack));

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
        assertThat(user.getCareer().getOccupation()).isEqualTo(dto.occupation());
        assertThat(user.getCareer().getYear()).isEqualTo(dto.year());
        assertThat(user.getTechStacks().stream().map(UserTechStack::getTechStack))
                .containsExactly(techStack.getTechStack());
    }

    @Test
    @WithUserDetails
    void 유저_삭제_성공() throws Exception {
        User user = prepareLoggedInActiveUser();

        mockMvc.perform(delete("/user"))
                .andExpectAll(
                        status().isOk()
                ).andDo(document("user/delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

        assertThat(user.getStatus()).isEqualTo(DELETED);
    }
}
