package com.codelap.api.controller.StudyNoticeComment;

import com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentDeleteDto.StudyNoticeCommentDeleteRequest;
import com.codelap.common.studyNoticeComment.service.StudyNoticeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentCreateDto.StudyNoticeCommentCreateRequest;

@RestController
@RequestMapping("/study-notice-comment")
@RequiredArgsConstructor
public class StudyNoticeCommentController {

    private final StudyNoticeCommentService studyNoticeCommentService;

    @PostMapping
    public void create(
            @RequestBody StudyNoticeCommentCreateRequest req
    ) {
        studyNoticeCommentService.create(req.studyNotice().toStudyNotice(), req.userId(), req.content());
    }

    @DeleteMapping
    public void delete(
            @RequestBody StudyNoticeCommentDeleteRequest req
    ) {
        studyNoticeCommentService.delete(req.studyNoticeCommentId(), req.userId());
    }
}
