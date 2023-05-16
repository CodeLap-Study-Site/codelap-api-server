package com.codelap.common.support;

import lombok.AllArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@AllArgsConstructor(access = PROTECTED)
public abstract class FileStandard {
    protected static final Long MIN_SIZE = 1L;
    public abstract FileStandard update(String savedName, String originalName, Long size);
}
