package com.codelap.api.controller.studyNotice.dto;

import com.codelap.common.studyNotice.domain.StudyNoticeFile;

import java.util.List;
import java.util.stream.Collectors;

public class StudyNoticeUpdateDto {
    public record StudyNoticeUpdateRequest(
            Long studyNoticeId,
            Long leaderId,
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
            String savedName,
            String originalName,
            Long size
    ) {
        public StudyNoticeFile toStudyNoticeFile() {
            return StudyNoticeFile.create(savedName, originalName, size);
        }
    }

}
