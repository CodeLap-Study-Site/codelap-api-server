package com.codelap.api.controller.studyComment;

import com.codelap.api.controller.studyComment.dto.StudyCommentUpdateDto.StudyCommentUpdateRequest;
import com.codelap.common.studyComment.service.StudyCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.codelap.api.controller.studyComment.dto.StudyCommentCreateDto.StudyCommentCreateRequest;

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
}
