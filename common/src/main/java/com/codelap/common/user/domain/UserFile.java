package com.codelap.common.user.domain;

import com.codelap.common.support.FileStandard;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codelap.common.support.Preconditions.require;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFile extends FileStandard {
    private String savedName;
    private String originalName;
    private Long size;

    protected static final Long MIN_SIZE = 1L;
    @Override
    public UserFile create(String savedName, String originalName, Long size) {
        require(isNotBlank(savedName));
        require(isNotBlank(originalName));

        return new UserFile(savedName, originalName, size);
    };
}


