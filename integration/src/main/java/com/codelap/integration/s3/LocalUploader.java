package com.codelap.integration.s3;

import com.codelap.common.support.FileStandard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.System.getProperty;

@RequiredArgsConstructor
@Profile({"local", "default"})
@Component
@Slf4j
public class LocalUploader implements FileUpload {

    private String fileDir = getProperty("user.dir");

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

        removeFile(getFullPath(storeFileName));

        return file.create(storeFileName, originalFileName, multipartFile.getSize());
    }

    public void removeFile(String fileName) {
        boolean delete = new File(fileName).delete();
        if(!delete) {
            throw new IllegalArgumentException("파일이 삭제되지 않았습니다.");
        }
        log.info("파일이 삭제되었습니다.");
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
