package com.codelap.api.controller.StudyNoticeComment;

import com.codelap.common.studyNoticeComment.service.StudyNoticeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
