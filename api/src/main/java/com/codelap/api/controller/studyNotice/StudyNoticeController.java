package com.codelap.api.controller.studyNotice;

import com.codelap.api.controller.studyNotice.dto.StudyNoticeDeleteDto.StudyNoticeDeleteRequest;
import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.common.studyNotice.service.StudyNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyNoticeCreateRequest req
    ) {
        studyNoticeService.create(req.studyId(), leader.getId(), req.title(), req.contents(), req.toStudyNoticeFiles());
    }

    @PostMapping("/update")
    public void update(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyNoticeUpdateRequest req
    ) {
        studyNoticeService.update(req.studyNoticeId(), leader.getId(), req.title(), req.contents(), req.toStudyNoticeFiles());
    }

    @DeleteMapping
    public void delete(
            @AuthenticationPrincipal DefaultCodeLapUser leader,
            @RequestBody StudyNoticeDeleteRequest req
    ) {
        studyNoticeService.delete(req.studyNoticeId(), leader.getId());
    }
}
