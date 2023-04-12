package com.codelap.api.controller.StudyNoticeComment.dto;

import com.codelap.common.study.domain.Study;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

public class StudyNoticeCommentCreateDto {
    public record StudyNoticeCommentCreateRequest(
            Long studyNoticeId,
            Long userId,
            String content
    ) {
    }
}
