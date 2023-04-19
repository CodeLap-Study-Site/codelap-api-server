package com.codelap.api.controller.studyNotice;

import com.codelap.api.controller.studyNotice.dto.StudyNoticeDeleteDto.StudyNoticeDeleteRequest;
import com.codelap.common.studyNotice.service.StudyNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.studyNotice.dto.StudyNoticeCreateDto.StudyNoticeCreateRequest;
import static com.codelap.api.controller.studyNotice.dto.StudyNoticeUpdateDto.StudyNoticeUpdateRequest;

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

    @PostMapping("/update")
    public void update(
            @RequestBody StudyNoticeUpdateRequest req
    ) {
        studyNoticeService.update(req.studyNoticeId(), req.leaderId(), req.title(), req.contents(), req.toStudyNoticeFiles());
    }

    @DeleteMapping
    public void delete(
            @RequestBody StudyNoticeDeleteRequest req
    ) {
        studyNoticeService.delete(req.studyNoticeId(), req.leaderId());
    }
}
