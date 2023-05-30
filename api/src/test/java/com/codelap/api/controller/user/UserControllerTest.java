package com.codelap.api.controller.user;

import com.codelap.api.controller.user.dto.UserActivateDto.UserActivateRequest;
import com.codelap.api.controller.user.dto.UserActivateDto.UserActivateRequestUserCareerDto;
import com.codelap.api.support.ApiTest;
import com.codelap.common.user.domain.User;
import com.codelap.common.user.domain.UserTechStack;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.List;

import static com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequest;
import static com.codelap.api.controller.user.dto.UserUpdateDto.UserUpdateRequestUserCareerDto;
import static com.codelap.api.support.RestDocumentationUtils.*;
import static com.codelap.common.support.TechStack.Java;
import static com.codelap.common.user.domain.UserStatus.ACTIVATED;
import static com.codelap.common.user.domain.UserStatus.DELETED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends ApiTest {

    private static final String DOCS_TAG = "User";

    @Test
    @WithUserDetails
    void 유저_활성화_성공() throws Exception {
        User user = prepareLoggedInUser();

        UserTechStack techStack = new UserTechStack(Java);
        UserActivateRequestUserCareerDto dto = new UserActivateRequestUserCareerDto("직무", 10);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/user/activate",
                        APPLICATION_JSON,
                        new UserActivateRequest("name", dto, List.of(techStack)),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "user/activate",
                        DOCS_TAG,
                        "유저 활성화",
                        null, null
                )
        ).andExpect(status().isOk());

        assertThat(user.getStatus()).isEqualTo(ACTIVATED);
        assertThat(user.getName()).isEqualTo("name");
        assertThat(user.getCareer().getOccupation()).isEqualTo(dto.occupation());
        assertThat(user.getCareer().getYear()).isEqualTo(dto.year());
        assertThat(user.getTechStacks().stream().map(UserTechStack::getTechStack))
                .containsExactly(techStack.getTechStack());
    }

    @Test
    @WithUserDetails
    void 유저_이미지_수정_성공() throws Exception {
        User user = prepareLoggedInActiveUser();

        mockMvc.perform(
                multipartRequestBuilder(
                        "/user/image-upload",
                        List.of(
                                new MockMultipartFile("multipartFile", "hello.txt", TEXT_PLAIN_VALUE, "Hello, World!".getBytes())
                        ),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "user/image-upload",
                        DOCS_TAG,
                        "유저 이미지 수정",
                        null, null
                )
        ).andExpect(status().isOk());


        assertThat(user.getFiles().get(0).getS3ImageURL()).isNotNull();
        assertThat(user.getFiles().get(0).getOriginalName()).isNotNull();
    }

    @Test
    @WithUserDetails
    void 유저_수정_성공() throws Exception {
        User user = prepareLoggedInActiveUser();

        UserTechStack techStack = new UserTechStack(Java);
        UserUpdateRequestUserCareerDto dto = new UserUpdateRequestUserCareerDto("직무", 10);

        mockMvc.perform(
                postMethodRequestBuilder(
                        "/user/update",
                        APPLICATION_JSON,
                        new UserUpdateRequest("updatedName", dto, List.of(techStack)),
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "user/update",
                        DOCS_TAG,
                        "유저 수정",
                        null, null
                )
        ).andExpect(status().isOk());

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


        mockMvc.perform(
                deleteMethodRequestBuilder(
                        "/user",
                        null,
                        null,
                        token
                )
        ).andDo(
                getRestDocumentationResult(
                        "user/delete",
                        DOCS_TAG,
                        "유저 삭제",
                        null, null
                )
        ).andExpect(status().isOk());

        assertThat(user.getStatus()).isEqualTo(DELETED);
    }

    @Test
    @WithUserDetails
    void 유저_활성화_상태_체크() throws Exception {
        prepareLoggedInActiveUser();

        mockMvc.perform(
                getMethodRequestBuilder(
                        "/user/is-activated",
                        token,
                        null
                )
        ).andDo(
                getRestDocumentationResult(
                        "user/is-activated",
                        DOCS_TAG,
                        "유저 활성화 상태 체크",
                        null, null
                )
        ).andExpectAll(
                status().isOk(),
                jsonPath("$").value(true)
        );
    }
}
