package com.codelap.api.controller.studyComment;

import com.codelap.api.controller.studyComment.dto.StudyCommentUpdateDto.StudyCommentUpdateRequest;
import com.codelap.api.security.user.DefaultCodeLapUser;
import com.codelap.common.studyComment.service.StudyCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.codelap.api.controller.studyComment.dto.StudyCommentCreateDto.StudyCommentCreateRequest;
import static com.codelap.api.controller.studyComment.dto.StudyCommentDeleteDto.StudyCommentDeleteRequest;

@RestController
@RequestMapping("/study-comment")
@RequiredArgsConstructor
public class StudyCommentController {

    private final StudyCommentService studyCommentService;

    @PostMapping
    public void create(
            @AuthenticationPrincipal DefaultCodeLapUser member,
            @RequestBody StudyCommentCreateRequest req
    ) {
        studyCommentService.create(req.studyId(), member.getId(), req.message());
    }

    @PostMapping("/update")
    public void update(
            @AuthenticationPrincipal DefaultCodeLapUser member,
            @RequestBody StudyCommentUpdateRequest req
    ) {
        studyCommentService.update(req.studyCommentId(), member.getId(), req.message());
    }

    @DeleteMapping
    public void delete(
            @AuthenticationPrincipal DefaultCodeLapUser member,
            @RequestBody StudyCommentDeleteRequest req
    ) {
        studyCommentService.delete(req.studyCommentId(), member.getId());
    }
}
