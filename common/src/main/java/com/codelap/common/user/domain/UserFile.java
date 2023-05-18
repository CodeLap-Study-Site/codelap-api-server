package com.codelap.common.user.domain;

import com.codelap.common.support.FileStandard;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.codelap.common.support.Preconditions.require;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFile extends FileStandard {
    private String s3ImageURL;
    private String originalName;
    public static final String dirName = "user";

    @Override
    public UserFile update(String s3ImageURL, String originalName) {
        require(isNotBlank(s3ImageURL));
        require(isNotBlank(originalName));

        this.s3ImageURL = s3ImageURL;
        this.originalName = originalName;

        return this;
    }

    public static FileStandard create() {
        return new UserFile();
    }
}


