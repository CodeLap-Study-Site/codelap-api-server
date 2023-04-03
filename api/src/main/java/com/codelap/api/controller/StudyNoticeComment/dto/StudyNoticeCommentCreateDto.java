package com.codelap.api.controller.StudyNoticeComment.dto;

import com.codelap.common.study.domain.Study;
import com.codelap.common.studyNotice.domain.StudyNotice;
import com.codelap.common.studyNotice.domain.StudyNoticeFile;

import java.util.List;
import java.util.stream.Collectors;

public class StudyNoticeCommentCreateDto {
    public record StudyNoticeCommentCreateRequest(
            StudyNoticeCommentCreateRequestStudyNoticeDto studyNotice,
            Long userId,
            String content
    ) {
    }

    public record StudyNoticeCommentCreateRequestStudyNoticeDto(
            Study study,
            String title,
            String contents,
            List<StudyNoticeCommentCreateRequestFileDto> files

    ) {
        public List<StudyNoticeFile> toStudyNoticeFile() {
            return files.stream()
                    .map(StudyNoticeCommentCreateRequestFileDto::toStudyNoticeFile)
                    .collect(Collectors.toList());
        }

        public StudyNotice toStudyNotice() {
            return StudyNotice.create(study, title, contents, toStudyNoticeFile());
        }
    }

    public record StudyNoticeCommentCreateRequestFileDto(
            String savedName,
            String originalName,
            Long size
    ) {
        public StudyNoticeFile toStudyNoticeFile() {
            return StudyNoticeFile.create(savedName, originalName, size);
        }
    }
}
