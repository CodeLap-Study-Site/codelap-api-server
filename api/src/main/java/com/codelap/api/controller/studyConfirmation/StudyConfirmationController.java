package com.codelap.api.controller.studyConfirmation;

import com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationConfirmDto.StudyConfirmationConfirmRequest;
import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.common.studyConfirmation.service.StudyConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationDeleteDto.StudyConfirmationDeleteRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationReConfirmDto.StudyConfirmationReConfirmRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationRejectDto.StudyConfirmationRejectRequest;

@RestController
@RequestMapping("/study-confirmation")
@RequiredArgsConstructor
public class StudyConfirmationController {

    private final StudyConfirmationService studyConfirmationService;

    @PostMapping
    public void create(
            @AuthenticationPrincipal DefaultCodeLapUser user,
            @RequestBody StudyConfirmationCreateRequest req
    ) {
        studyConfirmationService.create(req.studyId(), user.getId(), req.title(), req.content(), req.toStudyConfirmationFiles());
    }

    @PostMapping("/confirm")
    public void confirm(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyConfirmationConfirmRequest req
    ) {
        studyConfirmationService.confirm(req.studyConfirmId(), leader.getId());
    }

    @PostMapping("/reject")
    public void reject(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyConfirmationRejectRequest req
    ) {
        studyConfirmationService.reject(req.studyConfirmId(), leader.getId());
    }

    @PostMapping("/reconfirm")
    public void reConfirm(
            @AuthenticationPrincipal DefaultCodeLapUser user,
            @RequestBody StudyConfirmationReConfirmRequest req
    ) {
        studyConfirmationService.reConfirm(req.studyConfirmId(), user.getId(), req.title(), req.content(), req.toStudyReConfirmationFiles());
    }

    @DeleteMapping
    public void delete(
            @AuthenticationPrincipal DefaultCodeLapUser user,
            @RequestBody StudyConfirmationDeleteRequest req
    ) {
        studyConfirmationService.delete(req.studyConfirmId(), user.getId());
    }
}
