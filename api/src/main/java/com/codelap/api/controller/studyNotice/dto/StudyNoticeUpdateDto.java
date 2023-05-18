package com.codelap.api.controller.studyNotice.dto;

import com.codelap.common.studyNotice.domain.StudyNoticeFile;

import java.util.List;
import java.util.stream.Collectors;

public class StudyNoticeUpdateDto {
    public record StudyNoticeUpdateRequest(
            Long studyNoticeId,
            String title,
            String contents,
            List<StudyNoticeUpdateRequestFileDto> files
    ) {
        public List<StudyNoticeFile> toStudyNoticeFiles() {
            return files.stream()
                    .map(StudyNoticeUpdateRequestFileDto::toStudyNoticeFile)
                    .collect(Collectors.toList());
        }
    }

    public record StudyNoticeUpdateRequestFileDto(
            String s3ImageURL,
            String originalName
    ) {
        public StudyNoticeFile toStudyNoticeFile() {
            StudyNoticeFile studyNoticeFile = (StudyNoticeFile) StudyNoticeFile.create();
            return studyNoticeFile.update(s3ImageURL, originalName);
        }
    }

}
