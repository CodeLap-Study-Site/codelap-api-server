package com.codelap.api.controller.studyRequest;

import com.codelap.api.controller.studyRequest.dto.StudyRequestCreateDto;
import com.codelap.common.studyRequest.service.StudyRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.studyRequest.dto.StudyRequestCreateDto.*;

@RestController
@RequestMapping("/studyRequest")
@RequiredArgsConstructor
public class StudyRequestController {

    private final StudyRequestService studyRequestService;

    @PostMapping
    public void create(
            @RequestBody StudyRequestCreateRequest dto
    ) {
        studyRequestService.create(dto.userId(), dto.studyId(), dto.message());
    }
}
