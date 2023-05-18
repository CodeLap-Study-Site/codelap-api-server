package com.codelap.api.controller.studyNotice.dto;

import com.codelap.common.studyNotice.domain.StudyNoticeFile;

import java.util.List;
import java.util.stream.Collectors;

public class StudyNoticeCreateDto {

    public record StudyNoticeCreateRequest(
            Long studyId,
            String title,
            String contents,
            List<StudyNoticeCreateRequestFileDto> files
    ) {
        public List<StudyNoticeFile> toStudyNoticeFiles() {
            return files.stream()
                    .map(StudyNoticeCreateRequestFileDto::toStudyNoticeFile)
                    .collect(Collectors.toList());
        }
    }

    public record StudyNoticeCreateRequestFileDto(
            String s3ImageURL,
            String originalName
    ) {
        public StudyNoticeFile toStudyNoticeFile() {
            StudyNoticeFile studyNoticeFile = (StudyNoticeFile) StudyNoticeFile.create();
            return studyNoticeFile.update(s3ImageURL, originalName);
        }
    }
}
