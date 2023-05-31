package com.codelap.api.controller.study.dto;

import com.codelap.common.study.domain.StudyFile;

public class GetImageDto {
    public record GetImageResponse(
            String imageURL,
            String originalName
    ) {
        public static GetImageResponse create(StudyFile studyFile) {
            return new GetImageResponse(studyFile.getS3ImageURL(), studyFile.getOriginalName());
        }
    }
}
