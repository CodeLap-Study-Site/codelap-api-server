package com.codelap.api.controller.studyNotice.dto;

import com.codelap.common.studyNotice.domain.StudyNoticeFile;

import java.util.List;
import java.util.stream.Collectors;

public class StudyNoticeCreateDto {

    public record StudyNoticeCreateRequest(
            Long studyId,
            Long userId,
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
            String savedName,
            String originalName,
            Long size
    ) {
        public StudyNoticeFile toStudyNoticeFile() {
            return StudyNoticeFile.create(savedName, originalName, size);
        }
    }
}
