package com.codelap.api.controller.studyConfirmation.dto;

import com.codelap.common.studyConfirmation.domain.StudyConfirmationFile;

import java.util.List;
import java.util.stream.Collectors;

public class StudyConfirmationReConfirmDto {
    public record StudyConfirmationReConfirmRequest(
            Long studyConfirmId,
            String title,
            String content,
            List<StudyConfirmationReConfirmRequestFileDto> files
    ) {
        public List<StudyConfirmationFile> toStudyReConfirmationFiles() {
            return files.stream()
                    .map(StudyConfirmationReConfirmRequestFileDto::toStudyConfirmationFile)
                    .collect(Collectors.toList());
        }
    }

    public record StudyConfirmationReConfirmRequestFileDto(
            String savedName,
            String originalName,
            Long size
    ) {
        public StudyConfirmationFile toStudyConfirmationFile() {
            return (StudyConfirmationFile) StudyConfirmationFile.create();
        }
    }

}
