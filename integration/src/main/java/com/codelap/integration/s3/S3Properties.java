package com.codelap.integration.s3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class S3Properties {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
}
