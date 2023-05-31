package com.codelap.integration.s3;

import com.codelap.common.support.FileStandard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Profile({"local", "default"})
@Component
@Slf4j
public class LocalUploader implements FileUpload {
    @Override
    public FileStandard upload(MultipartFile multipartFile, String dirName, FileStandard file) {
        return file.update("s3ImageURL", "originalName");
    }

    @Override
    public List<FileStandard> uploads(List<MultipartFile> multipartFiles, String dirName, FileStandard file) {
        return null;
    }
}
