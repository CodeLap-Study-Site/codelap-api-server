package com.codelap.integration.s3;

import com.codelap.common.support.FileStandard;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Profile({"local", "default"})
@Component
public class LocalUploader implements FileUpload {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    @Override
    public List<FileStandard> uploads(List<MultipartFile> multipartFiles, String dirName, FileStandard file) throws IOException {
        List<FileStandard> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(upload(multipartFile, dirName, file));
            }
        }
        return storeFileResult;
    }
    @Override
    public FileStandard upload(MultipartFile multipartFile, String dirName, FileStandard file) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return file.create(storeFileName, originalFileName, multipartFile.getSize());
    }

    private String createStoreFileName(String originalFileName) {
        String ext = extractExt(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");

        return originalFileName.substring(pos + 1);
    }
}
