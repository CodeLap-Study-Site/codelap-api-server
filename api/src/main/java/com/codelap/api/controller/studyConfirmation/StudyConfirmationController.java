package com.codelap.api.controller.studyConfirmation;

import com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationConfirmDto.StudyConfirmationConfirmRequest;
import com.codelap.common.studyConfirmation.service.StudyConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationDeleteDto.StudyConfirmationDeleteRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationRejectDto.StudyConfirmationRejectRequest;
import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationreConfirmDto.StudyConfirmationreConfirmRequest;

@RestController
@RequestMapping("/study-confirmation")
@RequiredArgsConstructor
public class StudyConfirmationController {

    private final StudyConfirmationService studyConfirmationService;

    @PostMapping
    public void create(
            @RequestBody StudyConfirmationCreateRequest req
    ) {
        studyConfirmationService.create(req.studyId(), req.userId(), req.title(), req.content(), req.toStudyConfirmationFiles());
    }

    @PostMapping("/confirm")
    public void confirm(
            @RequestBody StudyConfirmationConfirmRequest req
    ) {
        studyConfirmationService.confirm(req.studyConfirmId(), req.leaderId());
    }

    @PostMapping("/reject")
    public void reject(
            @RequestBody StudyConfirmationRejectRequest req
    ) {
        studyConfirmationService.reject(req.studyConfirmId(), req.leaderId());
    }

    @PostMapping("/reconfirm")
    public void reConfirm(
            @RequestBody StudyConfirmationreConfirmRequest req
    ) {
        studyConfirmationService.reConfirm(req.studyConfirmId(), req.userId(), req.title(), req.content(), req.toStudyreConfirmationFiles());
    }

    @DeleteMapping
    public void delete(
            @RequestBody StudyConfirmationDeleteRequest req
    ) {
        studyConfirmationService.delete(req.studyConfirmId(), req.userId());
    }
}
