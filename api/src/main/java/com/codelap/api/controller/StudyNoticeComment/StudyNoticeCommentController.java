package com.codelap.api.controller.StudyNoticeComment;

import com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentDeleteDto.StudyNoticeCommentDeleteRequest;
import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.common.studyNoticeComment.service.StudyNoticeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentCreateDto.StudyNoticeCommentCreateRequest;
import static com.codelap.api.controller.StudyNoticeComment.dto.StudyNoticeCommentUpdateDto.StudyNoticeCommentUpdateReqeust;

@RestController
@RequestMapping("/study-notice-comment")
@RequiredArgsConstructor
public class StudyNoticeCommentController {

    private final StudyNoticeCommentService studyNoticeCommentService;

    @PostMapping
    public void create(
            @AuthenticationPrincipal DefaultCodeLapUser member,
            @RequestBody StudyNoticeCommentCreateRequest req
    ) {
        studyNoticeCommentService.create(req.studyNoticeId(), member.getId(), req.content());
    }

    @DeleteMapping
    public void delete(
            @AuthenticationPrincipal DefaultCodeLapUser member,
            @RequestBody StudyNoticeCommentDeleteRequest req
    ) {
        studyNoticeCommentService.delete(req.studyNoticeCommentId(), member.getId());
    }

    @PostMapping("/update")
    public void update(
            @AuthenticationPrincipal DefaultCodeLapUser member,
            @RequestBody StudyNoticeCommentUpdateReqeust req
    ) {
        studyNoticeCommentService.update(req.studyNoticeCommentId(), member.getId(), req.content());
    }
}
