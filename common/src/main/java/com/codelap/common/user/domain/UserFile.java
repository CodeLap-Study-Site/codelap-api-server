package com.codelap.common.user.domain;

import com.codelap.common.support.FileStandard;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import static com.codelap.common.support.Preconditions.require;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Getter
@Embeddable
public class UserFile extends FileStandard {
    private String savedName;
    private String originalName;
    private Long size;
    public static final String dirName = "user";

    @Override
    public UserFile update(String savedName, String originalName, Long size) {
        require(isNotBlank(savedName));
        require(isNotBlank(originalName));

        this.savedName = savedName;
        this.originalName = originalName;
        this.size = size;

        return this;
    }

    public static FileStandard create() {
        return new UserFile();
    }
}


