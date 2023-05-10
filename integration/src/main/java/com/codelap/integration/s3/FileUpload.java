package com.codelap.integration.s3;

import com.codelap.common.support.FileStandard;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUpload {
    FileStandard upload(MultipartFile multipartFile, String dirName, FileStandard file) throws IOException;

    List<FileStandard> uploads(List<MultipartFile> multipartFile, String dirName, FileStandard file) throws IOException;
}
