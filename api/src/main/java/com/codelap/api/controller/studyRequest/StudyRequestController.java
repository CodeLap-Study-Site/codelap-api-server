package com.codelap.api.controller.studyRequest;

import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.api.service.studyrequest.StudyRequestAppService;
import com.codelap.common.studyRequest.service.StudyRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.studyRequest.dto.StudyRequestApproveDto.StudyRequestApproveRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestCancelDto.StudyRequestCancelRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestCreateDto.StudyRequestCreateRequest;
import static com.codelap.api.controller.studyRequest.dto.StudyRequestRejectDto.StudyRequestRejectRequest;

@RestController
@RequestMapping("/study-request")
@RequiredArgsConstructor
public class StudyRequestController {

    private final StudyRequestService studyRequestService;
    private final StudyRequestAppService studyRequestAppService;

    @PostMapping
    public void create(
            @AuthenticationPrincipal DefaultCodeLapUser user,
            @RequestBody StudyRequestCreateRequest dto
    ) {
        studyRequestService.create(user.getId(), dto.studyId(), dto.message());
    }

    @PostMapping("approve")
    public void approve(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyRequestApproveRequest dto
    ) {
        studyRequestAppService.approve(dto.studyRequestId(), leader.getId());
    }

    @PostMapping("reject")
    public void reject(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyRequestRejectRequest dto
    ) {
        studyRequestService.reject(dto.studyRequestId(), leader.getId(), dto.rejectMessage());
    }

    @PostMapping("cancel")
    public void cancel(
            @AuthenticationPrincipal DefaultCodeLapUser user,
            @RequestBody StudyRequestCancelRequest dto
    ) {
        studyRequestService.cancel(dto.studyRequestId(), user.getId());
    }
}
