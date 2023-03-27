package com.codelap.api.controller.studyNotice;

import com.codelap.common.studyNotice.service.StudyNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.studyNotice.dto.StudyNoticeCreateDto.StudyNoticeCreateRequest;

@RestController
@RequestMapping("/study-notice")
@RequiredArgsConstructor
public class StudyNoticeController {

    private final StudyNoticeService studyNoticeService;

    @PostMapping
    public void create(
            @RequestBody StudyNoticeCreateRequest req
    ) {
        studyNoticeService.create(req.studyId(), req.userId(), req.title(), req.contents(), req.toStudyNoticeFiles());
    }
}
