package com.codelap.integration.s3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class S3Properties {
    private String credentialAccessKey;
    private String credentialSecretKey;
    private String regionStatic;
    private String s3Bucket;
}
