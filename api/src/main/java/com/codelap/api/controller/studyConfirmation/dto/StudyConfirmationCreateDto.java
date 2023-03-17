package com.codelap.api.controller.studyConfirmation.dto;

import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;

import java.util.List;
import java.util.stream.Collectors;

public class StudyConfirmationCreateDto {

    public record StudyConfirmationCreateRequest(
            Long studyId,
            Long userId,
            String title,
            String content,
            List<StudyConfirmationCreateRequestFileDto> files
    ) {
        public List<StudyConfirmationFile> toStudyConfirmationFiles() {
            return files.stream()
                    .map(StudyConfirmationCreateRequestFileDto::toStudyConfirmationFile)
                    .collect(Collectors.toList());
        }
    }

    public record StudyConfirmationCreateRequestFileDto(
            String savedName,
            String originalName,
            Long size
    ) {
        public StudyConfirmationFile toStudyConfirmationFile() {
            return StudyConfirmationFile.create(savedName, originalName, size);
        }
    }
}
