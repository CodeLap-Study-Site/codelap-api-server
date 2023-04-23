package com.codelap.api.controller.studyComment;

import com.codelap.api.controller.studyComment.dto.StudyCommentUpdateDto.StudyCommentUpdateRequest;
import com.codelap.common.studyComment.service.StudyCommentService;
import lombok.RequiredArgsConstructor;
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
            @RequestBody StudyCommentCreateRequest req
    ) {
        studyCommentService.create(req.studyId(), req.userId(), req.message());
    }

    @PostMapping("/update")
    public void update(
            @RequestBody StudyCommentUpdateRequest req
    ) {
        studyCommentService.update(req.studyCommentId(), req.userId(), req.message());
    }

    @DeleteMapping
    public void delete(
            @RequestBody StudyCommentDeleteRequest req
    ) {
        studyCommentService.delete(req.studyCommentId(), req.userId());
    }
}
