package com.codelap.api.controller.study.dto;

import com.codelap.common.study.domain.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StudyCreateDto {

    public record StudyCreateRequest(
            String name,
            String info,
            int maxMembersSize,
            StudyDifficulty difficulty,
            StudyCreateRequestStudyPeriodDto period,
            StudyCreateRequestStudyNeedCareerDto career,
            List<StudyTechStack> techStackList,
            List<StudyCreateRequestStudyFileDto> studyFiles
    ) {
        public List<StudyFile> toStudyFiles() {
            return studyFiles.stream().map(StudyCreateRequestStudyFileDto::toStudyFile)
                    .collect(Collectors.toList());
        }
    }

    public record StudyCreateRequestStudyPeriodDto(
            OffsetDateTime startAt,
            OffsetDateTime endAt
    ) {
        public StudyPeriod toStudyPeriod() {
            return StudyPeriod.create(startAt, endAt);
        }
    }

    public record StudyCreateRequestStudyNeedCareerDto(
            String occupation,
            int year
    ) {
        public StudyNeedCareer toStudyNeedCareer() {
            return StudyNeedCareer.create(occupation, year);
        }
    }

    public record StudyCreateRequestStudyFileDto(
            String imageURL,
            String originalName
    ) {
        public StudyFile toStudyFile() {
            StudyFile studyFile = StudyFile.create();
            return studyFile.update(imageURL, originalName);
        }
    }
}
