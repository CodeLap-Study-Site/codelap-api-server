package com.codelap.api.controller.studyConfirmation.dto;

import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;

import java.util.List;
import java.util.stream.Collectors;

public class StudyConfirmationreConfirmDto {
    public record StudyConfirmationreConfirmRequest(
            Long studyConfirmId,
            Long userId,
            String title,
            String content,
            List<StudyConfirmationreConfirmRequestFileDto> files
    ) {
        public List<StudyConfirmationFile> toStudyreConfirmationFiles() {
            return files.stream()
                    .map(StudyConfirmationreConfirmRequestFileDto::toStudyConfirmationFile)
                    .collect(Collectors.toList());
        }
    }

    public record StudyConfirmationreConfirmRequestFileDto(
            String savedName,
            String originalName,
            Long size
    ) {
        public StudyConfirmationFile toStudyConfirmationFile() {
            return StudyConfirmationFile.create(savedName, originalName, size);
        }
    }

}
