package com.codelap.api.controller.studyRequest;

import com.codelap.api.service.studyrequest.StudyRequestAppService;
import com.codelap.common.studyRequest.service.StudyRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.studyRequest.dto.StudyRequestApproveDto.StudyRequestApproveRequest;
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
            @RequestBody StudyRequestCreateRequest dto
    ) {
        studyRequestService.create(dto.userId(), dto.studyId(), dto.message());
    }

    @PostMapping("approve")
    public void approve(
            @RequestBody StudyRequestApproveRequest dto
    ) {
        studyRequestAppService.approve(dto.studyRequestId(), dto.leaderId());
    }

    @PostMapping("reject")
    public void reject(
            @RequestBody StudyRequestRejectRequest dto
    ) {
        studyRequestService.reject(dto.studyRequestId(), dto.leaderId(), dto.rejectMessage());
    }
}
