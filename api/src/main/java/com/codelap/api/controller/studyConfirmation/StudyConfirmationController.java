package com.codelap.api.controller.studyConfirmation;

import com.codelap.common.studyConfirmation.service.StudyConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.studyConfirmation.dto.StudyConfirmationCreateDto.StudyConfirmationCreateRequest;

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
}
