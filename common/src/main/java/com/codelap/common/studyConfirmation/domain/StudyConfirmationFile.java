package com.codelap.common.studyConfirmation.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codelap.common.support.Preconditions.require;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Getter
@Embeddable
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class StudyConfirmationFile {
    private String savedName;
    private String originalName;
    private Long size;

    private static final Long MIN_SIZE = 1L;

    public static StudyConfirmationFile create(String savedName, String originalName, Long size) {
        require(isNotBlank(savedName));
        require(isNotBlank(originalName));
        require(size > MIN_SIZE);

        return new StudyConfirmationFile(savedName, originalName, size);
    }
}
