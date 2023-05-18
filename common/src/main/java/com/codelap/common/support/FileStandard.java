package com.codelap.common.support;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
public abstract class FileStandard {
    public abstract FileStandard update(String s3ImageURL, String originalName);
}
