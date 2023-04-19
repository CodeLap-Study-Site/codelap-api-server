package com.codelap.api.controller.studyComment;

import com.codelap.api.controller.studyComment.dto.StudyCommentUpdateDto;
import com.codelap.api.controller.studyComment.dto.StudyCommentUpdateDto.StudyCommentUpdateRequest;
import com.codelap.common.studyComment.service.StudyCommentService;
import com.codelap.common.studyNoticeComment.service.StudyNoticeCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/study-comment")
@RequiredArgsConstructor
public class StudyCommentController {

    private final StudyCommentService studyCommentService;

    @PostMapping("/update")
    public void update(
            @RequestBody StudyCommentUpdateRequest req
            ){
        studyCommentService.update(req.studyCommentId(), req.userId(), req.comment());
    }
}
